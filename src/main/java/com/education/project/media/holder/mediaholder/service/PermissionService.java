package com.education.project.media.holder.mediaholder.service;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface PermissionService {
    boolean allowed(@NotNull UUID userID,
                    @NotNull Operation operation) throws Exception;
}
