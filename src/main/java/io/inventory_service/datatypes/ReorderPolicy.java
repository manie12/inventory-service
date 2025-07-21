package io.inventory_service.datatypes;

public enum ReorderPolicy {
    ROP,            // demand × lead time + safety stock
    MIN_MAX,        // order-up-to level
    EOQ             // economic order quantity
}
