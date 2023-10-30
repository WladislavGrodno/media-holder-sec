package com.education.project.media.holder.mediaholder.integration.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ExternalUserLevel(
        UUID id,
        String levelDescr
){}
