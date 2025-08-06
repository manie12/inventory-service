package io.inventory_service.datatypes;

public enum RuleStatus {
    ACTIVE,     // Rule is currently in effect
    INACTIVE,   // Rule is temporarily disabled
    DRAFT,      // Rule is being configured, not yet active
    ARCHIVED    // Rule is no longer in use
}
