package com.education.project.media.holder.mediaholder.model.media;

import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(
        name = "${integration.profile.mediaService.name}",
        url = "${integration.profile.mediaService.url}",
        path = "${integration.profile.mediaService.context-path}"
)
public interface MediaClient {
    @PostMapping("/media")
    ResponseEntity<MediaInfoResponse> createMedia(
            @ModelAttribute MediaRequest media,
            @RequestBody MultipartFile fileBody,
            @RequestParam UUID userID
    ) throws Exception;


}
