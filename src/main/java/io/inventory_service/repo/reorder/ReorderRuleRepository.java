package io.inventory_service.repo.reorder;

import io.inventory_service.datatypes.ReorderPolicy;
import io.inventory_service.models.ReorderRule;
import io.inventory_service.repo.customs.ReorderRuleRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface ReorderRuleRepository
        extends ReactiveCrudRepository<ReorderRule, UUID>,
        ReorderRuleRepositoryCustom {

    /* Look-ups */

    Mono<ReorderRule> findByRuleCode(String ruleCode);

    Flux<ReorderRule> findBySkuAndVariantCodeAndWarehouseId(
            String sku, String variantCode, String warehouseId);

    Flux<ReorderRule> findByPolicy(ReorderPolicy policy);

    Flux<ReorderRule> findByActiveIsTrueAndEffectiveFromBeforeAndEffectiveToAfter(
            Instant ts1, Instant ts2);
}