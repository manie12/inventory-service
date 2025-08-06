package io.inventory_service.datatypes;

public enum ReservationStatus {

    ACTIVE,
    EXPIRED,
    CANCELLED,
    FULFILLED,// e.g., for future transfer
    OTHER
}
