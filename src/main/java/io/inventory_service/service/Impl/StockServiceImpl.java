package io.inventory_service.service.Impl;

import io.inventory_service.config.MetricsConfig;
import io.inventory_service.repo.StockItemRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockServiceImpl {

    private final StockItemRepository repo;
    private final ReactiveStringRedisTemplate redis;
    private final KafkaTemplate<String, Object> kafka;
    private final MetricsConfig metrics;        // <-- inject

    @Value("${kafka.topics.stock-reserved}")
    private String topicReserved;

    @Value("${kafka.topics.stock-released}")
    private String topicReleased;

    @Value("${kafka.topics.stock-reserved}")
    private String topicReserved;

    @Value("${kafka.topics.stock-released}")
    private String topicReleased;

    // -------------- RESERVE --------------------------------------------------
    @Timed(value = "inventory_reserve_latency_seconds",              // OTEL + Micrometer
            longTask = false, extraTags = {"outcome", "unknown"})
    @Transactional
    public Mono<Boolean> reserve(UUID tenant, String sku, String loc, int qty, UUID orderId) {

        String mutex = "lock:" + tenant + ':' + sku + ':' + loc;

        return redis.opsForValue()
                .setIfAbsent(mutex, "1", Duration.ofSeconds(5))
                .flatMap(acquired -> {
                    if (!acquired) {
                        // metr
                        // ic: lock contention
                        metrics.getRedisLockContention().increment();
                        return Mono.just(false);
                    }

                    return repo.findByTenantIdAndSkuAndLocation(tenant, sku, loc)
                            .flatMap(item -> repo.tryReserve(item.getId(), qty))
                            .map(rows -> rows == 1)
                            .doOnNext(ok -> {
                                Tag outcome = ok ? Tag.of("outcome", "SUCCESS")
                                        : Tag.of("outcome", "FAIL");
                                // record latency sample with correct tag
                                metrics.getReserveLatency().record(outcome);

                                if (ok) publish(topicReserved, tenant, sku, loc, qty, orderId);
                                redis.delete(mutex).subscribe();
                            });
                });
    }

    @Transactional
    public Mono<Boolean> release(UUID tenant, String sku, String loc, int qty, UUID orderId) {
        return repo.findByTenantIdAndSkuAndLocation(tenant, sku, loc)
                .flatMap(item -> repo.releaseReserve(item.getId(), qty))
                .map(rows -> rows == 1)
                .doOnNext(ok -> {
                    if (ok) publish(topicReleased, tenant, sku, loc, qty, orderId);
                });
    }

    @Transactional
    public Mono<List<UUID>> reserveUnits(UUID tenant, String sku, String loc,
                                         int qty, UUID orderId) {

        return unitRepo.reserveUnitIds(tenant, sku, loc, qty)
                .collectList()
                .flatMap(ids -> {
                    if (ids.size() != qty)
                        return Mono.error(new IllegalStateException("Not enough units"));

                    publish(topicReserved, tenant, sku, loc, qty, orderId,
                            Map.of("unitIds", ids));
                    return Mono.just(ids);
                });
    }

    @Transactional
    public Mono<Boolean> releaseUnits(UUID tenant, String sku, String loc,
                                      List<UUID> unitIds, UUID orderId) {

        return unitRepo.releaseUnitIds(tenant, sku, loc, unitIds)
                .count()
                .map(cnt -> cnt == unitIds.size())
                .doOnNext(ok -> {
                    if (ok)
                        publish(topicReleased, tenant, sku, loc, unitIds.size(),
                                orderId, Map.of("unitIds", unitIds));
                });
    }

    // -------------- ADJUST ---------------------------------------------------
    public Mono<Void> adjust(UUID tenant, String sku, String loc, int delta,
                             AdjustmentReason reason, String comment) {

        return repo.adjustOnHand(tenant, sku, loc, delta)
                .then(Mono.fromRunnable(() -> {
                    metrics.getInventoryAdjustCounter()
                            .tag("reason", reason.name())
                            .increment(Math.abs(delta));

                    kafka.send("stock.adjusted.v1", Map.of(
                            "tenantId", tenant,
                            "sku", sku,
                            "location", loc,
                            "delta", delta,
                            "reason", reason.name(),
                            "comment", comment
                    ));
                }));
    }
}