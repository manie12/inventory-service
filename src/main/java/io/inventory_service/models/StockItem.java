package io.inventory_service.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("stock_items")
public class StockItem {

    @Id
    private UUID id;

    private UUID tenantId;
    private String sku;
    private String location;

    private int onHand;
    private int reserved;
    private int version;

    private Instant createdAt;
    private Instant updatedAt;
}