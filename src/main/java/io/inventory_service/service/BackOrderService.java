package io.inventory_service.service;

import io.inventory_service.dtos.BackOrder;
import io.inventory_service.repo.BackOrderRepository;
import io.inventory_service.repo.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackOrderService {

    private final BackOrderRepository queueRepo;
    private final InventoryItemRepository stockRepo;
    private final KafkaTemplate<String, Object> kafka;

    /**
     * topic names injected from YAML
     */
    @Value("${kafka.topics.backorder-queued}")
    private String topicQueued;
    @Value("${kafka.topics.backorder-fulfilled}")
    private String topicFulfilled;
    @Value("${kafka.topics.stock-added}")
    private String topicStockAdded;

    /* ---------- API used by Order-Service when reserve fails ---------- */

    public Mono<Void> queueBackOrder(UUID tenant, UUID orderId,
                                     String sku, String loc, int qty) {

        return queueRepo.save(BackOrder.builder()
                        .tenantId(tenant)
                        .orderId(orderId)
                        .sku(sku)
                        .location(loc)
                        .qtyNeeded(qty)
                        .build())
                .doOnSuccess(bo -> publish(topicQueued, bo))
                .then();
    }

    /* ---------- Listener: whenever stock increases we try to fulfill ---------- */

    @KafkaListener(topics = "${kafka.topics.stock-added}", groupId = "inventory-service")
    public void onStockAdded(ConsumerRecord<String, Map<String, Object>> msg) {

        Map<String, Object> ev = msg.value();
        String sku = (String) ev.get("sku");
        String loc = (String) ev.get("location");
        int deltaQty = (int) ev.get("qty");          // +ve number

        log.debug("🔥 Stock added event [{} +{}]", sku, deltaQty);

        fulfillFromQueue(sku, loc, deltaQty).subscribe();
    }

    /**
     * Atomically pop oldest back-orders up to available quantity.
     */
    @Transactional
    public Mono<Void> fulfillFromQueue(String sku, String loc, int qtyAvailable) {

        return queueRepo.findBySkuAndLocationOrderByQueuedAtAsc(sku, loc)
                .flatMap(bo -> tryFulfill(bo, qtyAvailable))
                .takeUntil(res -> res.qtyAvailable == 0)
                .then();
    }

    /* ---------- helper record ---------- */
    private record Result(int qtyAvailable) {
    }

    private Mono<Result> tryFulfill(BackOrder bo, int qtyAvail) {

        int toReserve = Math.min(qtyAvail, bo.getQtyNeeded());
        if (toReserve == 0) return Mono.just(new Result(qtyAvail));

        return stockRepo.tryReserveBySkuAndLocation(bo.getSku(), bo.getLocation(), toReserve)
                .filter(rows -> rows == 1)
                .flatMap(ok -> {
                    // delete back-order row if fully satisfied
                    Mono<Void> cleanup = (toReserve == bo.getQtyNeeded())
                            ? queueRepo.deleteById(bo.getId())
                            : queueRepo.save(bo.toBuilder()
                            .qtyNeeded(bo.getQtyNeeded() - toReserve)
                            .build()).then();

                    return cleanup.doOnSuccess(v ->
                            publish(topicFulfilled, bo, toReserve));
                })
                .thenReturn(new Result(qtyAvail - toReserve))
                .defaultIfEmpty(new Result(qtyAvail)); // reserve failed (race) -> skip
    }

    private void publish(String topic, BackOrder bo) {
        publish(topic, bo, bo.getQtyNeeded());
    }

    private void publish(String topic, BackOrder bo, int qty) {
        kafka.send(topic, Map.of(
                "tenantId", bo.getTenantId(),
                "orderId", bo.getOrderId(),
                "sku", bo.getSku(),
                "location", bo.getLocation(),
                "qty", qty
        ));
    }
}