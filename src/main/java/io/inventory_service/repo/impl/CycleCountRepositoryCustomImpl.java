package io.inventory_service.repo.impl;

import io.inventory_service.models.count.CycleCountItem;
import io.inventory_service.models.count.CycleCountSession;
import io.inventory_service.repo.countCycle.CycleCountItemRepository;
import io.inventory_service.repo.customs.CycleCountRepositoryCustom;
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
public class CycleCountRepositoryCustomImpl implements CycleCountRepositoryCustom {

    private final R2dbcEntityTemplate entityTemplate;
    private final CycleCountItemRepository cycleCountItemRepository;
    private final TransactionalOperator txOp;

    @Override
    public Mono<CycleCountSession> insertWithLines(CycleCountSession header,
                                                   List<CycleCountItem> lines) {
        return entityTemplate.insert(CycleCountSession.class)
                .using(header)
                .flatMap(saved ->
                        Flux.fromIterable(lines)
                                .map(l -> {
                                    l.setId(saved.getId());
                                    return l;
                                })
                                .flatMap(cycleCountItemRepository::save)
                                .then(Mono.just(saved))
                )
                .as(txOp::transactional);
    }

    @Override
    public Mono<CycleCountSession> upsertByCode(CycleCountSession header,
                                                List<CycleCountItem> lines) {
        return entityTemplate.select(CycleCountSession.class)
                .matching(query(where("session_code").is(header.getCycleSessionCode())))
                .one()
                .switchIfEmpty(insertWithLines(header, lines));
    }

    @Override
    public Mono<CycleCountSession> findByIdWithItems(UUID id) {
        return entityTemplate.selectOne(query(where("id").is(id)), CycleCountSession.class)
                .flatMap(cycleCount -> cycleCountItemRepository.findBySessionId(cycleCount.getId())
                        .collectList()
                        .map(items -> {
                            cycleCount.setItems(items); // Set items on the header
                            return cycleCount;
                        }))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<CycleCountSession> findByCountCodeWithItems(String countCode) {
        return entityTemplate.selectOne(query(where("count_code").is(countCode)), CycleCountSession.class)
                .flatMap(cycleCount -> cycleCountItemRepository.findBySessionId(cycleCount.getId())
                        .collectList()
                        .map(items -> {
                            cycleCount.setItems(items); // Set items on the header
                            return cycleCount;
                        }))
                .switchIfEmpty(Mono.empty());
    }
}
