package io.inventory_service.datatypes;

public enum EventType {
    // Inbound receipts
    STOCK_RECEIVED_V1("stock.received.v1"),

    // Reservations placed
    STOCK_RESERVED_V1("stock.reserved.v1"),

    // Reservations released
    STOCK_RELEASED_V1("stock.released.v1"),

    // Manual adjustments
    STOCK_ADJUSTED_V1("stock.adjusted.v1"),

    // Backorder requests
    BACKORDER_QUEUED_V1("backorder.queued.v1"),

    // Backorder fulfillments
    BACKORDER_FULFILLED_V1("backorder.fulfilled.v1");

    private final String topicName; // Field to store the associated Kafka topic name

    EventType(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    /**
     * Optional: Helper method to get an EventType enum from its topic name string.
     * This can be useful when consuming Kafka messages.
     *
     * @param topic The Kafka topic name string.
     * @return The corresponding EventType enum, or null if no match is found.
     */
    public static EventType fromTopicName(String topic) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getTopicName().equalsIgnoreCase(topic)) {
                return eventType;
            }
        }
        // Consider throwing IllegalArgumentException or returning Optional<EventType>
        // depending on how you want to handle unknown topics.
        return null; // Or throw new IllegalArgumentException("Unknown topic: " + topic);
    }
}