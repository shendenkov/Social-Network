package com.example.socialnetwork.user.event;

import java.util.UUID;

public record UserRegisteredEvent(
  UUID eventId,
  UUID userId
) implements DomainEvent {
}