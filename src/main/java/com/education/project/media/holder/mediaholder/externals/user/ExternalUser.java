package com.education.project.media.holder.mediaholder.externals.user;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.UUID;

public record ExternalUser(
    @Schema(name = "id", description = "User id", example = "086d792e-7974-4fe4-b2e0-2dba9f79bed8")
    UUID Id,
    @Schema (name = "firstName", description = "User firstname", example = "John")
    String firstName,
    @Schema (name = "lastName", description = "User lastname", example = "Smith")
    String lastName,
    @Schema (name = "password", description = "User password", example = "Gib5!?jEs#")
    String password,
    @Schema (name = "email", description = "User email", example = "abcdefg@gmail.com")
    String email,
    @Schema (name = "phone", description = "User phone number", example = "+375334455667")
    String phone,
    @Schema (name = "roleId", description = "User role", example = "086d792e-7974-4fe4-b2e0-2dba9f79bed8")
    ExternalUserRole role,
    @Schema (name = "levelId", description = "User level", example = "086d792e-7974-4fe4-b2e0-2dba9f79bed8")
    ExternalUserLevel level,
    @Schema (name = "createdAt", description = "User creation date", example = "2017-05-14T10:34")
    Timestamp createdAt,
    @Schema (name = "modificationAt", description = "User modification date", example = "2017-05-14T10:34")
    Timestamp modificationAt
){}
