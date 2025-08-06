package io.inventory_service.repo.impl;

import io.inventory_service.models.release.InventoryReservationRelease;
import io.inventory_service.models.release.InventoryReservationReleaseItem;
import io.inventory_service.repo.customs.InventoryReservationReleaseRepositoryCustom;
import io.inventory_service.repo.release.InventoryReservationReleaseItemRepository;
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
public class InventoryReservationReleaseRepositoryCustomImpl implements InventoryReservationReleaseRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final InventoryReservationReleaseItemRepository inventoryReservationReleaseItemRepository;
    private final TransactionalOperator txOp;

    @Override
    public Mono<InventoryReservationRelease> insertWithItems(InventoryReservationRelease header,
                                                             List<InventoryReservationReleaseItem> items) {
        return entityTemplate
                .insert(InventoryReservationRelease.class)
                .using(header)
                .flatMap(saved ->
                        Flux.fromIterable(items)
                                .map(i -> {
                                    i.setReleaseId(saved.getId());
                                    return i;
                                })
                                .flatMap(inventoryReservationReleaseItemRepository::save)
                                .then(Mono.just(saved))
                )
                .as(txOp::transactional);
    }

    @Override
    public Mono<InventoryReservationRelease> upsertByCode(InventoryReservationRelease header,
                                                          List<InventoryReservationReleaseItem> items) {
        return entityTemplate
                .select(InventoryReservationRelease.class)
                .matching(query(where("release_code").is(header.getReleaseCode())))
                .one()
                .switchIfEmpty(insertWithItems(header, items));
    }

    @Override
    public Mono<InventoryReservationRelease> findByIdWithItems(UUID id) {
        return entityTemplate
                .selectOne(query(where("id").is(id)), InventoryReservationRelease.class)
                .flatMap(releaseEvent -> inventoryReservationReleaseItemRepository.findByReleaseId(releaseEvent.getId())
                        .collectList()
                        .map(items -> {
                            releaseEvent.setItems(items); // Set items on the header
                            return releaseEvent;
                        }))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<InventoryReservationRelease> findByReleaseCodeWithItems(String releaseCode) {
        return entityTemplate
                .selectOne(query(where("release_code").is(releaseCode)), InventoryReservationRelease.class)
                .flatMap(releaseEvent -> inventoryReservationReleaseItemRepository.findByReleaseId(releaseEvent.getId())
                        .collectList()
                        .map(items -> {
                            releaseEvent.setItems(items);
                            return releaseEvent;
                        }))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<InventoryReservationRelease> findByReservationId(UUID reservationId) {
        // For finding all release events for a reservation, we don't necessarily need items initially.
        // If items are consistently needed, adjust this.
        return entityTemplate
                .select(query(where("reservation_id").is(reservationId)), InventoryReservationRelease.class);
        // If you need items eagerly:
        // return entityTemplate.select(query(where("reservation_id").is(reservationId)), ReservationReleaseEvent.class)
        //         .flatMap(releaseEvent -> itemRepo.findByReleaseEventId(releaseEvent.getId())
        //                 .collectList()
        //                 .map(items -> {
        //                     releaseEvent.setItems(items);
        //                     return releaseEvent;
        //                 }));
    }
}
