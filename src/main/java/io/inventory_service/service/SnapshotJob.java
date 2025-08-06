package io.inventory_service.service;

import io.inventory_service.models.InventorySnapshot;
import io.inventory_service.repo.InventorySnapshotRepository;
import io.inventory_service.repo.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Slf4j
@Configuration
@EnableScheduling          // enables Spring’s lightweight scheduler
@RequiredArgsConstructor
public class SnapshotJob {

    private final InventoryItemRepository stockRepo;
    private final InventorySnapshotRepository snapRepo;
    private final KafkaTemplate<String, Object> kafka;

    @Value("${inventory.snapshot.kafka-topic:inventory-snapshot}")
    private String topic;

    /**
     * Fires according to cron (defaults hourly).
     */
    @Scheduled(cron = "${inventory.snapshot.cron:0 0 * * * *}")
    public void captureSnapshot() {

        Instant now = Instant.now();

        stockRepo.findAll()
                // convert each bucket row → snapshot entity
                .map(item -> InventorySnapshot.builder()
                        .sku(item.getSku())
                        .location(item.getLocation())
                        .onHand(item.getOnHand())
                        .reserved(item.getReserved())
                        .capturedAt(now)
                        .build())
                .collectList()
                .flatMapMany(snapRepo::saveAll)       // bulk insert
                .doOnNext(snap ->
                        kafka.send(new ProducerRecord<>(
                                topic, snap.getSku(), snap)))
                .then()    // we don’t care about downstream result
                .doOnSuccess(v ->
                        log.info("📸  Captured inventory snapshot @{}", now))
                .doOnError(e ->
                        log.error("Failed to capture inventory snapshot", e))
                .subscribe();   // kick off reactive pipeline (fire-and-forget)
    }
}