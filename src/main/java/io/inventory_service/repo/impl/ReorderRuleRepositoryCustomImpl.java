package io.inventory_service.repo.impl;

import io.inventory_service.models.ReorderRule;
import io.inventory_service.repo.customs.ReorderRuleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class ReorderRuleRepositoryCustomImpl
        implements ReorderRuleRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<ReorderRule> upsertByCode(ReorderRule rule) {

        return template
                .select(ReorderRule.class)
                .matching(query(where("rule_code").is(rule.getRuleCode())))
                .one()
                .switchIfEmpty(
                        template.insert(ReorderRule.class).using(rule)
                )
                .flatMap(existing -> {
                    /* if already present → update mutable fields, keep immutable ones */
                    existing.setPolicy(rule.getPolicy());
                    existing.setReviewFrequency(rule.getReviewFrequency());
                    existing.setSafetyStockUnits(rule.getSafetyStockUnits());
                    existing.setLeadTimeDays(rule.getLeadTimeDays());
                    existing.setAverageDailyDemand(rule.getAverageDailyDemand());
                    existing.setMinStockUnits(rule.getMinStockUnits());
                    existing.setMaxStockUnits(rule.getMaxStockUnits());
                    existing.setReorderPointUnits(rule.getReorderPointUnits());
                    existing.setReorderQuantityUnits(rule.getReorderQuantityUnits());
                    existing.setMinOrderQuantity(rule.getMinOrderQuantity());
                    existing.setOrderMultiple(rule.getOrderMultiple());
                    existing.setPreferredSupplierId(rule.getPreferredSupplierId());
                    existing.setActive(rule.getActive());
                    existing.setEffectiveFrom(rule.getEffectiveFrom());
                    existing.setEffectiveTo(rule.getEffectiveTo());
                    existing.setExtraParams(rule.getExtraParams());
                    existing.onUpdate(rule.getUpdatedBy());        // stamps audit fields
                    return template.update(ReorderRule.class).using(existing);
                });
    }
}