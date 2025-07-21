package io.inventory_service.datatypes;

public enum ReservationStatus {
    SALES_ORDER,
    WORK_ORDER,
    SUBSCRIPTION_BOX,
    TRANSFER_ALLOCATION,
    ACTIVE,
    EXPIRED,
    CANCELLED,
    FULFILLED,// e.g., for future transfer
    OTHER
}
