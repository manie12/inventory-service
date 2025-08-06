package io.inventory_service.repo.impl;

import io.inventory_service.models.transfer.InventoryTransfer;
import io.inventory_service.models.transfer.InventoryTransferItem;
import io.inventory_service.repo.customs.InventoryTransferRepositoryCustom;
import io.inventory_service.repo.receipt.InventoryReceiptItemRepository;
import io.inventory_service.repo.transfer.InventoryTransferItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryTransferRepositoryCustomImpl
        implements InventoryTransferRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final InventoryTransferItemRepository inventoryTransferItemRepository;
    private final TransactionalOperator txOp;

    @Override
    public Mono<InventoryTransfer> insertWithItems(InventoryTransfer header,
                                                   List<InventoryTransferItem> items) {

        return entityTemplate
                .insert(InventoryTransfer.class)
                .using(header)                      // 1️⃣ save header
                .flatMap(savedHeader ->
                        Flux.fromIterable(items)     // 2️⃣ save items (FK set)
                                .map(item -> {
                                    item.setInventoryTransferId(savedHeader.getId());
                                    item.onCreate();
                                    return item;
                                })
                                .flatMap(inventoryTransferItemRepository::save)
                                .then(Mono.just(savedHeader))
                )
                .as(txOp::transactional);           // 3️⃣ single DB tx
    }

    @Override
    public Mono<InventoryTransfer> upsertByCode(InventoryTransfer header,
                                                List<InventoryTransferItem> items) {

        return entityTemplate
                .select(InventoryTransfer.class)
                .matching(query(where("transfer_code").is(header.getTransferCode())))
                .one()
                .switchIfEmpty(insertWithItems(header, items));
    }
}