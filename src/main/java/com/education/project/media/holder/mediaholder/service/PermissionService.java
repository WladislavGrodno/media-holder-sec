package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.enums.Operation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface PermissionService {
    boolean allowed(@NotNull UUID userID,
                    @NotNull Operation operation) throws Exception;
}
