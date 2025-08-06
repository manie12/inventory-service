package io.inventory_service.datatypes;

public enum ReleaseReason {
    PAYMENT_FAILED,
    CUSTOMER_CANCELLED,
    ORDER_REPLACED,
    TTL_EXPIRED,
    PICKED_AND_SHIPPED,
    OTHER
}