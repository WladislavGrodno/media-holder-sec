package com.education.project.media.holder.mediaholder.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

public record MediaInfoResponse(

        @Schema(description = "File ID",
                example = "1525430455740003903")
        UUID id,

        //todo: change description
        @Schema(description =
                "Upload time (milliseconds after 1970.01.01 Greenwich)",
                example = "1525430455740003903")
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
