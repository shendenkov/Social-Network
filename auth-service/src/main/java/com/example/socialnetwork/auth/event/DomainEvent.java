package com.example.socialnetwork.auth.event;

import java.util.UUID;

public interface DomainEvent {
  UUID eventId();
}