package io.github.latcn.archbase.foundation.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseDomainEvent {
    private final String eventId;
    private final Instant occurredAt;
    private final String sourceId;

    protected BaseDomainEvent(String sourceId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.sourceId = sourceId;
    }

    protected BaseDomainEvent(String sourceId, String eventId, Instant occurredAt) {
        this.sourceId = sourceId;
        this.eventId = eventId;
        this.occurredAt = occurredAt;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getSourceId() {
        return sourceId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{eventId=" + eventId + ", sourceId=" + sourceId + "}";
    }
}