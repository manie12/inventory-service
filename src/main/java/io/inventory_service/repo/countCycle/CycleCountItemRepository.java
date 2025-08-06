package io.inventory_service.repo.countCycle;

import io.inventory_service.datatypes.CountStatus;
import io.inventory_service.models.count.CycleCountItem;
import io.inventory_service.repo.customs.CycleCountItemRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface CycleCountItemRepository
        extends ReactiveCrudRepository<CycleCountItem, Long>,
        CycleCountItemRepositoryCustom {

    Flux<CycleCountItem> findBySessionId(UUID sessionId);

    Flux<CycleCountItem> findBySessionIdAndStatus(UUID sessionId, CountStatus status);
}