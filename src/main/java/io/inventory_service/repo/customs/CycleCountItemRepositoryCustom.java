package io.inventory_service.repo.customs;

import io.inventory_service.models.count.CycleCountItem;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CycleCountItemRepositoryCustom {

    Flux<CycleCountItem> bulkInsert(List<CycleCountItem> lines);
}