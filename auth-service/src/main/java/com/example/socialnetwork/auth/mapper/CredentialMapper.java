package com.example.socialnetwork.auth.mapper;

import com.example.socialnetwork.auth.dto.request.RegisterRequest;
import com.example.socialnetwork.auth.dto.response.RegisterResponse;
import com.example.socialnetwork.auth.entity.Credential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CredentialMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "publicId", expression = "java(UUID.randomUUID())")
  @Mapping(target = "passwordHash", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Credential toCredential(RegisterRequest request);

  @Mapping(target = "id", source = "publicId")
  RegisterResponse toRegisterResponse(Credential credential);
}