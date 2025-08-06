package io.inventory_service.repo.customs;

import io.inventory_service.models.count.CycleCountItem;
import io.inventory_service.models.count.CycleCountSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CycleCountRepositoryCustom {

    Mono<CycleCountSession> insertWithLines(CycleCountSession header,
                                            List<CycleCountItem> items);

    Mono<CycleCountSession> upsertByCode(CycleCountSession header,
                                         List<CycleCountItem> items);

    /**
     * Finds a CycleCount by its internal UUID and eagerly fetches its associated items.
     *
     * @param id The internal UUID of the CycleCount.
     * @return A Mono emitting the CycleCount with its items, or empty if not found.
     */
    Mono<CycleCountSession> findByIdWithItems(UUID id);

    /**
     * Finds a CycleCount by its unique count code and eagerly fetches its associated items.
     *
     * @param countCode The unique count code.
     * @return A Mono emitting the CycleCount with its items, or empty if not found.
     */
    Mono<CycleCountSession> findByCountCodeWithItems(String countCode);
}