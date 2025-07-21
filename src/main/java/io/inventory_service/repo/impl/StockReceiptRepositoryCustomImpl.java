package io.inventory_service.repo.impl;

import io.inventory_service.models.StockReceipt;
import io.inventory_service.models.StockReceiptItem;
import io.inventory_service.repo.StockReceiptItemRepository;
import io.inventory_service.repo.customs.StockReceiptRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
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
public class StockReceiptRepositoryCustomImpl implements StockReceiptRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final StockReceiptItemRepository stockReceiptItemRepository;
    private final TransactionalOperator txOp;   // injected via @Bean configuration

    @Override
    public Mono<StockReceipt> insertWithItems(StockReceipt header,
                                              List<StockReceiptItem> items) {

        return entityTemplate                      // 1️⃣  save header
                .insert(StockReceipt.class)
                .using(header)
                .flatMap(savedHeader ->            // 2️⃣  save lines with FK
                        Flux.fromIterable(items)
                                .map(item -> {             // set FK before insert
                                    item.setReceiptId(savedHeader.getId());
                                    item.onCreate(); // Assuming onCreate initializes item ID and timestamps
                                    return item;
                                })
                                .flatMap(stockReceiptItemRepository::save)
                                .then(Mono.just(savedHeader))
                )
                .as(txOp::transactional);          // 3️⃣  single DB tx
    }

    @Override
    public Mono<StockReceipt> upsertById(StockReceipt header,
                                         List<StockReceiptItem> lines) {
        return entityTemplate
                .select(StockReceipt.class)
                .matching(query(where("receipt_id").is(header.getReceiptId())))
                .one()
                .switchIfEmpty(insertWithItems(header, lines))
                .flatMap(Mono::just); // already there
    }
}