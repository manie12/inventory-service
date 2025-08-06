package io.inventory_service.repo.impl;

import io.inventory_service.models.restock.InventoryReturn;
import io.inventory_service.models.restock.InventoryReturnItem;
import io.inventory_service.repo.customs.InventoryReturnRepositoryCustom;
import io.inventory_service.repo.restock.InventoryReturnItemRepository;
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
public class InventoryReturnRepositoryCustomImpl
        implements InventoryReturnRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final InventoryReturnItemRepository itemRepo;
    private final TransactionalOperator txOp;

    @Override
    public Mono<InventoryReturn> insertWithItems(InventoryReturn header,
                                                 List<InventoryReturnItem> items) {
        return entityTemplate.insert(InventoryReturn.class)
                .using(header)
                .flatMap(saved ->
                        Flux.fromIterable(items)
                                .map(i -> {
                                    i.setId(saved.getId());
                                    return i;
                                })
                                .flatMap(itemRepo::save)
                                .then(Mono.just(saved))
                )
                .as(txOp::transactional);
    }

    @Override
    public Mono<InventoryReturn> upsertByCode(InventoryReturn header,
                                              List<InventoryReturnItem> items) {
        return entityTemplate.select(InventoryReturn.class)
                .matching(query(where("return_code").is(header.getReturnCode())))
                .one()
                .switchIfEmpty(insertWithItems(header, items));
    }

    @Override
    public Mono<InventoryReturn> findByIdWithItems(UUID id) {
        return entityTemplate
                .selectOne(query(where("id").is(id)), InventoryReturn.class)
                .flatMap(restock -> itemRepo.findByReturnId(restock.getId())
                        .collectList()
                        .map(items -> {
                            restock.setItems(items); // Set items on the header
                            return restock;
                        }))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<InventoryReturn> findByReturnCodeWithItems(String returnCode) {
        return entityTemplate.selectOne(query(where("return_code").is(returnCode)), InventoryReturn.class)
                .flatMap(restock -> itemRepo.findByReturnId(restock.getId())
                        .collectList()
                        .map(items -> {
                            restock.setItems(items); // Set items on the header
                            return restock;
                        }))
                .switchIfEmpty(Mono.empty());
    }
}