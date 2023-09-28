package com.education.project.media.holder.mediaholder.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.io.Resource;

import java.time.Instant;
//import java.time.LocalDateTime;
import java.util.UUID;

public record MediaResponse(

        @Schema(description = "File ID",
                example = "c8d385d8-652d-4851-b015-5aa94a8ffe19")
        UUID id,

        @Schema(description =
                "Upload time",
                example = "2023-09-24T08:10:19.270612Z")
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
                example = "12345")
        long fileSize,

        //ToDo - change adequate variable type
        @Schema(description = "File body"
                //, example = "12345"
        )
        Resource fileBody
) {}
