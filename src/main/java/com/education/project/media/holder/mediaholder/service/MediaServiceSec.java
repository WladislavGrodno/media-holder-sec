package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.dto.request.MediaInfoRequest;
import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.model.DataPage;
import com.education.project.media.holder.mediaholder.model.MediaSearchCriteria;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MediaServiceSec {

    ResponseEntity<MediaInfoResponse> createMedia(
            @NotNull MediaRequest media,
            @NotNull UUID userID
    ) throws Exception;


    ResponseEntity<Resource> getMediaById(
            @NotNull UUID id,
            @NotNull UUID userId
    ) throws Exception;


    ResponseEntity<MediaInfoResponse> getMediaInfoById(
            @NotNull UUID id,
            @NotNull UUID userId
    ) throws Exception;


    ResponseEntity<MediaInfoResponse> updateMediaById(
            @NotNull UUID id,
            @NotNull MultipartFile mediaFile,
            @NotNull UUID userId
    ) throws Exception;

    ResponseEntity<MediaInfoResponse> updateMediaInfoById(
            @NotNull UUID id,
            @NotNull MediaInfoRequest mediaInfo,
            @NotNull UUID userId
    ) throws Exception;

    void eraseMedia(@NotNull UUID id,
                    @NotNull UUID userId
    ) throws Exception;

    ResponseEntity<Page<MediaInfoResponse>> mediaCustomListRead(
            @NotNull DataPage carPage,
            @NotNull MediaSearchCriteria carSearchCriteria,
            @NotNull UUID userId
    );
}