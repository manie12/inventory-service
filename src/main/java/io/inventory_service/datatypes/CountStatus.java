package io.inventory_service.datatypes;

public enum CountStatus {
    INITIATED,      // Count record created, but counting hasn't started
    IN_PROGRESS,    // Counting is actively being performed
    PENDING_REVIEW, // Counting complete, awaiting review and approval of discrepancies
    REVIEWED,       // Review completed, adjustments may or may not be created
    COMPLETED,      // Count process finalized, all necessary adjustments made or decisions recorded
    CANCELLED       // Count was cancelled before completion


}
