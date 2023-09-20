package com.education.project.media.holder.mediaholder.integration.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record ExternalUserRole(
    @Schema(name = "id", description = "Role id", example = "086d792e-7974-4fe4-b2e0-2dba9f79bed8")
    UUID id,
    @Schema (name = "roleDescr", description = "Description of the role", example = "administrator")
    String roleDescr
){}
