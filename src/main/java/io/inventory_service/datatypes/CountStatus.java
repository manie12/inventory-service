package io.inventory_service.datatypes;

public enum CountStatus {
    CYCLE,          // targeted subset (ABC logic)
    FULL,           // entire warehouse shutdown
    SPOT,            // ad-hoc recount
    INITIATED,    // Count assignment created
    IN_PROGRESS,  // Counting is actively happening
    PENDING_REVIEW, // Count data entered, awaiting variance review
    COMPLETED,    // Variances resolved, adjustments made
    CANCELLED

}
