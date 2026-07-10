package com.example.socialnetwork.user.event;

import java.util.UUID;

public interface DomainEvent {
  UUID eventId();
}