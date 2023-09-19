package com.education.project.media.holder.mediaholder.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record MediaRequest(

        @Schema(description = "File nickname",
                example = "Media1")
        @NotNull(message = "Blank value name is denied")
        @Size(max = 256)
        String name,

        @Schema(description = "File description",
                example = "Very interesting media")
        @Size(max = 512)
        String description,

        @Schema(description = "Type of media: 1 - IMG, 2 - AUD, 3 - VID",
                example = "2")
        @NotNull(message = "Null value type is denied")
        int type,

        @Schema(description = "Media file",
                example = "MultipartFile X3 insert in swagger"
        )
        @NotNull(message = "Null file is denied")
        MultipartFile fileBody
) {}
