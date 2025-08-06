package io.inventory_service.repo.impl;

import io.inventory_service.models.reservation.InventoryReservation;
import io.inventory_service.models.reservation.InventoryReservationItem;
import io.inventory_service.repo.customs.InventoryReservationRepositoryCustom;
import io.inventory_service.repo.receipt.InventoryReceiptRepository;
import io.inventory_service.repo.reservation.InventoryReservationItemRepository;
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
public class InventoryReservationRepositoryCustomImpl
        implements InventoryReservationRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final InventoryReservationItemRepository inventoryReservationItemRepository;
    private final TransactionalOperator txOp;   // configured in a @Bean elsewhere

    @Override
    public Mono<InventoryReservation> insertWithItems(InventoryReservation header,
                                                      List<InventoryReservationItem> items) {

        return entityTemplate
                .insert(InventoryReservation.class)
                .using(header)                           // 1️⃣  save header
                .flatMap(saved ->
                        Flux.fromIterable(items)          // 2️⃣  save items
                                .map(it -> {
                                    it.setReservationId(saved.getId());
                                    return it;
                                })
                                .flatMap(inventoryReservationItemRepository::save)
                                .then(Mono.just(saved))
                )
                .as(txOp::transactional);                // 3️⃣  single DB tx
    }

    @Override
    public Mono<InventoryReservation> findByIdWithItems(UUID id) {
        return entityTemplate
                .selectOne(query(where("id").is(id)), InventoryReservation.class)
                .flatMap(reservation -> inventoryReservationItemRepository.findByReservationId(reservation.getId())
                        .collectList()
                        .map(items -> {
                            reservation.setItems(items);
                            return reservation;
                        }))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<InventoryReservation> findByReservationCodeWithItems(String reservationCode) {
        return entityTemplate.selectOne(query(where("reservation_code").is(reservationCode)), InventoryReservation.class)
                .flatMap(reservation -> inventoryReservationItemRepository.findByReservationId(reservation.getId())
                        .collectList()
                        .map(items -> {
                            reservation.setItems(items);
                            return reservation;
                        }))
                .switchIfEmpty(Mono.empty());
    }


@Override
public Mono<InventoryReservation> upsertByCode(InventoryReservation header,
                                               List<InventoryReservationItem> items) {
    return entityTemplate
            .select(InventoryReservation.class)
            .matching(query(where("reservation_code").is(header.getReservationCode())))
            .one()
            .switchIfEmpty(insertWithItems(header, items));
}
}