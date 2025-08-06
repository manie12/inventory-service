package io.inventory_service.repo.countCycle;

import io.inventory_service.datatypes.CycleCountStatus;
import io.inventory_service.models.count.CycleCountSession;
import io.inventory_service.repo.customs.CycleCountRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface CycleCountRepository
        extends ReactiveCrudRepository<CycleCountSession, UUID>,
        CycleCountRepositoryCustom {

    Mono<CycleCountSession> findBySessionCode(String sessionCode);

    Flux<CycleCountSession> findByWarehouseIdAndScheduledAtBetween(
            String warehouseId, Instant from, Instant to);

    Flux<CycleCountSession> findByStatus(CycleCountStatus status);
}