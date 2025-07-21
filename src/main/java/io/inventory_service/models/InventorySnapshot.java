package io.inventory_service.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_snapshots")
public class InventorySnapshot {

    @Id
    private UUID id;

    private String sku;
    private String location;

    private int onHand;
    private int reserved;

    private Instant capturedAt;
}