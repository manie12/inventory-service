package io.inventory_service.repo.impl;

import io.inventory_service.models.receipt.InventoryReceipt;
import io.inventory_service.models.receipt.InventoryReceiptItem;
import io.inventory_service.repo.receipt.InventoryReceiptItemRepository;
import io.inventory_service.repo.customs.InventoryReceiptRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class InventoryReceiptRepositoryCustomImpl implements InventoryReceiptRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final InventoryReceiptItemRepository inventoryReceiptItemRepository;
    private final TransactionalOperator txOp;   // injected via @Bean configuration

    @Override
    public Mono<InventoryReceipt> insertWithItems(InventoryReceipt header,
                                                  List<InventoryReceiptItem> items) {

        return entityTemplate                      // 1️⃣  save header
                .insert(InventoryReceipt.class)
                .using(header)

                .flatMap(savedHeader ->            // 2️⃣  save lines with FK
                        Flux.fromIterable(items)
                                .map(item -> {             // set FK before insert
                                    item.setReceiptId(savedHeader.getId());
                                    item.onCreate(); // Assuming onCreate initializes item ID and timestamps
                                    return item;
                                })
                                .flatMap(inventoryReceiptItemRepository::save)
                                .then(Mono.just(savedHeader))
                )
                .as(txOp::transactional);          // 3️⃣  single DB tx
    }

    @Override
    public Mono<InventoryReceipt> upsertById(InventoryReceipt header,
                                             List<InventoryReceiptItem> lines) {
        return entityTemplate
                .select(InventoryReceipt.class)
                .matching(query(where("receipt_id").is(header.getReceiptId())))
                .one()
                .switchIfEmpty(insertWithItems(header, lines))
                .flatMap(Mono::just); // already there
    }
}