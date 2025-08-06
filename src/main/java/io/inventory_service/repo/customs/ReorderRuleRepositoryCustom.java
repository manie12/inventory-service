package io.inventory_service.repo.customs;

import io.inventory_service.models.ReorderRule;
import reactor.core.publisher.Mono;

/**
 * Extra behaviours beyond simple CRUD.
 */
public interface ReorderRuleRepositoryCustom {

    /**
     * Idempotent upsert keyed by ruleCode so callers can retry safely.
     */
    Mono<ReorderRule> upsertByCode(ReorderRule rule);
}