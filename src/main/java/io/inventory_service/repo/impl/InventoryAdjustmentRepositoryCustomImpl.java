package io.inventory_service.repo.impl;

import io.inventory_service.models.adjustment.InventoryAdjustment;
import io.inventory_service.models.adjustment.InventoryAdjustmentItem;
import io.inventory_service.repo.adjustment.InventoryAdjustmentItemRepository;
import io.inventory_service.repo.customs.InventoryAdjustmentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class InventoryAdjustmentRepositoryCustomImpl
        implements InventoryAdjustmentRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final InventoryAdjustmentItemRepository itemRepo;
    private final TransactionalOperator txOp;

    @Override
    public Mono<InventoryAdjustment> insertWithItems(InventoryAdjustment header,
                                                     List<InventoryAdjustmentItem> items) {
        return entityTemplate.insert(InventoryAdjustment.class)
                .using(header)
                .flatMap(saved ->
                        Flux.fromIterable(items)
                                .map(i -> {
                                    i.setAdjustmentId(saved.getId());
                                    return i;
                                })
                                .flatMap(itemRepo::save)
                                .then(Mono.just(saved))
                )
                .as(txOp::transactional);
    }

    @Override
    public Mono<InventoryAdjustment> upsertByCode(InventoryAdjustment header,
                                                  List<InventoryAdjustmentItem> items) {
        return entityTemplate.select(InventoryAdjustment.class)
                .matching(query(where("adjustment_code").is(header.getAdjustmentCode())))
                .one()
                .switchIfEmpty(insertWithItems(header, items));
    }

    @Override
    public Mono<InventoryAdjustment> findByIdWithItems(UUID id) {
        return entityTemplate.selectOne(query(where("id").is(id)), InventoryAdjustment.class)
                .flatMap(adjustment -> itemRepo.findByAdjustmentId(adjustment.getId())
                        .collectList()
                        .map(items -> {
                            adjustment.setItems(items); // Set items on the header
                            return adjustment;
                        }))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<InventoryAdjustment> findByAdjustmentCodeWithItems(String adjustmentCode) {
        return entityTemplate.selectOne(query(where("adjustment_code").is(adjustmentCode)), InventoryAdjustment.class)
                .flatMap(adjustment -> itemRepo.findByAdjustmentId(adjustment.getId())
                        .collectList()
                        .map(items -> {
                            adjustment.setItems(items);
                            return adjustment;
                        }))
                .switchIfEmpty(Mono.empty());
    }

}