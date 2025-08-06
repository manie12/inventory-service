package io.inventory_service.datatypes;

public enum ReturnReason {
    DEFECTIVE,
    DAMAGED_IN_TRANSIT,
    WRONG_ITEM_SENT,
    NOT_AS_DESCRIBED,
    EXCESS_STOCK,       // supplier returns
    WARRANTY,
    OTHER
}