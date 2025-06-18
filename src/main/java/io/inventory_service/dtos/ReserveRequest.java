package io.inventory_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class ReserveRequest {
    UUID tenantId;
    String sku;
    String location;
    int qty;
    UUID orderId;
}