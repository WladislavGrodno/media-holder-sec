package com.education.project.media.holder.mediaholder.dto.response;

import java.util.HashMap;
import java.util.Map;

public record StatusResponse(
        String status,
        String message,
        Map<String, Object> details
) {
    public StatusResponse(String status, String message) {
        this(status, message, new HashMap<>());
    }
}
