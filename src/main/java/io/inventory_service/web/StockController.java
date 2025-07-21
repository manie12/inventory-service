
package io.inventory_service.web;

import io.inventory_service.dtos.ReserveRequest;
import io.inventory_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService service;

    @PostMapping("/reserve")
    public Mono<ResponseEntity<Void>> reserve(@RequestHeader("X-Tenant-Id") String tenantId,
                                              @RequestBody ReserveRequest r) {
        r.setTenantId(UUID.fromString(tenantId));
        return service.reserve(r.getTenantId(), r.getSku(), r.getLocation(), r.getQty(), r.getOrderId())
                .map(ok -> ok ? ResponseEntity.accepted().build()
                        : ResponseEntity.status(409).build());
    }

    @PostMapping("/release")
    public Mono<ResponseEntity<Void>> release(@RequestBody ReserveRequest r) {
        return service.release(r.getTenantId(), r.getSku(), r.getLocation(), r.getQty(), r.getOrderId())
                .map(ok -> ok ? ResponseEntity.noContent().build()
                        : ResponseEntity.status(304).build());
    }

    @PostMapping("/reserve/units")
    public Mono<UnitReserveResponse> reserveUnits(@RequestBody UnitReserveRequest r) {
        return stockService.reserveUnits(r.tenantId(), r.sku(), r.location(),
                        r.unitIds().size(), r.orderId())
                .map(ids -> new UnitReserveResponse(true, ids));
    }
}
