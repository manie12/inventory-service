package io.inventory_service.datatypes;

public enum AppliesToType {
    SKU_AND_LOCATION,  // Rule for a specific SKU in a specific location
    SKU_ONLY,          // Rule for a SKU across all locations (or default)
    LOCATION_ONLY,     // Rule for all SKUs in a specific location
    VENDOR_SKU         // Rule for a SKU from a specific vendor
    // This could also be implicitly handled by nullability of SKU/location IDs
}
