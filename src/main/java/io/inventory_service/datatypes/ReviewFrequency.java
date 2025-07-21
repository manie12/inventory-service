package io.inventory_service.datatypes;

public enum ReviewFrequency {
    CONTINUOUS,     // every event (stream processing)
    DAILY,
    WEEKLY,
    MONTHLY
}
