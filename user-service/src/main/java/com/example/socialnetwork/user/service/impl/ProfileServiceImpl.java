package com.example.socialnetwork.user.service.impl;

import com.example.socialnetwork.user.entity.ProcessedEvent;
import com.example.socialnetwork.user.entity.Profile;
import com.example.socialnetwork.user.event.UserRegisteredEvent;
import com.example.socialnetwork.user.repository.ProcessedEventRepository;
import com.example.socialnetwork.user.repository.ProfileRepository;
import com.example.socialnetwork.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
  private final ProfileRepository profileRepository;
  private final ProcessedEventRepository processedEventRepository;

  @Override
  public void createProfile(UserRegisteredEvent event) {
    if (processedEventRepository.existsById(event.eventId())) {
      return;
    }

    profileRepository.save(
      Profile.builder()
        .userId(event.userId())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build()
    );

    processedEventRepository.save(
      ProcessedEvent.builder()
        .eventId(event.eventId())
        .processedAt(Instant.now())
        .build()
    );
  }
}