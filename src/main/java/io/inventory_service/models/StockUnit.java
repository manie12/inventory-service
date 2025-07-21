package io.inventory_service.models;

import io.inventory_service.datatypes.UnitState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("stock_units")
public class StockUnit {

    @Id private UUID id;

    private UUID tenantId;
    private String sku;
    private String location;

    private String serialNo;
    private String lotNo;
    private LocalDate expiryDate;

    private UnitState state;

    private Instant createdAt;
    private Instant updatedAt;
}