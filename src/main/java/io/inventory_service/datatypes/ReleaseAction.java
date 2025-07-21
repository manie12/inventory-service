package io.inventory_service.datatypes;

public enum ReleaseAction {
    FULFILL,      // Convert reservation to an actual outbound shipment/pick
    CANCEL,       // Cancel the reservation, making stock available
    EXPIRE        // System-triggered expiry (less common via API, more internal)

}
