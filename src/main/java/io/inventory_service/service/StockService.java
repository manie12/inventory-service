package io.inventory_service.service;

import io.inventory_service.repo.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockItemRepository repo;
    private final ReactiveStringRedisTemplate redis;
    private final KafkaTemplate<String,Object> kafka;

    @Value("${kafka.topics.stock-reserved}") private String topicReserved;
    @Value("${kafka.topics.stock-released}") private String topicReleased;

    /** Attempt to reserve stock; returns true if successful */
    @Transactional
    public Mono<Boolean> reserve(UUID tenant, String sku, String loc, int qty, UUID orderId) {
        String mutex = "lock:" + tenant + ':' + sku + ':' + loc;

        return redis.opsForValue()
                .setIfAbsent(mutex, "1", Duration.ofSeconds(5))
                .flatMap(acquired -> {
                    if (!acquired) return Mono.just(false);

                    return repo.findByTenantIdAndSkuAndLocation(tenant, sku, loc)
                            .flatMap(item -> repo.tryReserve(item.getId(), qty))
                            .map(rows -> rows == 1)
                            .doOnNext(ok -> {
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

    private void publish(String topic, UUID tenant, String sku, String loc, int qty, UUID orderId) {
        kafka.send(topic, Map.of(
                "tenantId", tenant,
                "sku",      sku,
                "location", loc,
                "qty",      qty,
                "orderId",  orderId
        ));
    }
}