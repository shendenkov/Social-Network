package com.example.socialnetwork.auth.event;

import java.util.UUID;

public record UserRegisteredEvent(
  UUID eventId,
  UUID userId
) implements DomainEvent {
}