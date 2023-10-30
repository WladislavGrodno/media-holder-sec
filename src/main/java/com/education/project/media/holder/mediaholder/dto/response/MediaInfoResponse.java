package com.education.project.media.holder.mediaholder.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record MediaInfoResponse(
        @Schema(description = "File ID",
                example = "856f9c0d-cd80-489a-85cf-8905e6b5d6b0")
        UUID id,
        @Schema(description = "Upload time",
                example = "2023-10-28T07:55:26.681025Z")
        Instant uploadTime,
        @Schema(description = "File nickname",
                example = "Media1")
        String name,
        @Schema(description = "File description",
                example = "Very interesting media")
        String description,
        @Schema(description = "Type of media: 1 - IMG, 2 - AUD, 3 - VID",
                example = "2")
        int type,
        @Schema(description = "File name",
                example = "whistle12345.wav")
        String fileName,
        @Schema(description = "File size",
                example = "10242401")
        long fileSize
) {}
