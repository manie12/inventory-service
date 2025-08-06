package io.inventory_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID id;
    private String sku;
    private String title;
    private String qty;                // as per your field; consider int if numeric
    private String description;
    private long priceMinor;
    private String currency;
    private Attributes attributes;
    private List<VariantDto> variants;
    private List<String> categoryIds;
    private String status;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attributes {
        private String color;
        private String brand;
        private String batteryLife;      // map from "battery_life"
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantDto {
        private String code;
        private long priceDeltaMinor;
        private Attributes attributes;   // reusing same Attributes class
    }
}