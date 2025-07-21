package io.inventory_service.dtos;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("back_order_queue")
public class BackOrder {

    @Id
    private UUID id;
    private UUID tenantId;
    private UUID orderId;
    private String sku;
    private String location;
    private int qtyNeeded;
    private Instant queuedAt;
}