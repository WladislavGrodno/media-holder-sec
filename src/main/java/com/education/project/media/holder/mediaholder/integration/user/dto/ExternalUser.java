package com.education.project.media.holder.mediaholder.integration.user.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record ExternalUser(
    UUID Id,
    String firstName,
    String lastName,
    String password,
    String email,
    String phone,
    ExternalUserRole roleDtoResp,
    ExternalUserLevel levelDtoResp,
    Timestamp createdAt,
    Timestamp modificationAt
){}
