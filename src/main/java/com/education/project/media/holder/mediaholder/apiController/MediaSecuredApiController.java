package com.education.project.media.holder.mediaholder.apiController;

import com.education.project.media.holder.mediaholder.dto.request.MediaInfoRequest;
import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.service.MediaServiceSec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * CRUD operations Secured media store
 */
@RestController
@Tag(name = "Media-service-sec API")
public class MediaSecuredApiController {
    @Autowired
    @Qualifier("mediaServiceSecImp")
    private MediaServiceSec mediaService;

    @PostMapping("/media")
    @Operation(
            summary = "Add a new media to storage by authorized user",
            description = "Add a new media in to storage by authorized user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "The media was not added")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<MediaInfoResponse> createMedia(
            @Valid @ModelAttribute MediaRequest media,
            @Valid @RequestParam UUID userID
    ) throws Exception {
        return mediaService.createMedia(media, userID);
    }


    //ready
    /**
     * Return media by id
     * @param id identifier of requested media file
     * @return requested media file
     */
    @GetMapping(
            "/media/{id}"
            //value = "/media/{id}",
            //produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @Operation(
            summary = "Find a media by ID",
            description = "Return a media by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "The media was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    public ResponseEntity<Resource> getMediaById(
            @Valid @PathVariable UUID id,
            @Valid @RequestParam UUID userID
    ) throws Exception {
        return mediaService.getMediaById(id, userID);
    }


    //ready
    /**
     * Return media info by id
     * @param id identifier of requested media file
     * @return requested media info file
     */
    @GetMapping("/media-info/{id}")
    @Operation(
            summary = "Get a media info by ID",
            description = "Return a media info by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "The media was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    public ResponseEntity<MediaInfoResponse> getMediaInfoById(
            @Valid @PathVariable UUID id,
            @Valid @RequestParam UUID userID
    ) throws Exception {
        return mediaService.getMediaInfoById(id, userID);
    }


    //ready
    /**
     * update media by ID
     * @param id identifier of updated media
     * @param mediaFile updated media
     * @return updated media info
     */
    @PutMapping("/media/{id}")
    @Operation(
            summary = "Update an existing media",
            description = "Update an existing media by Id")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "The media was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    public ResponseEntity<MediaInfoResponse> updateMedia(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody MultipartFile mediaFile,
            @Valid @RequestParam UUID userID
    ) throws Exception {
        return mediaService.updateMediaById(id, mediaFile, userID);
    }


    //ready
    /**
     * update media info by ID
     * @param mediaInfo updated media info
     * @param id identifier of updated media
     * @return updated media info
     */
    @PutMapping("/media-info/{id}")
    @Operation(
            summary = "Update an existing media info",
            description = "Update an existing media info by Id")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "The media was not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    public ResponseEntity<MediaInfoResponse> updateMediaInfo(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody MediaInfoRequest mediaInfo,
            @Valid @RequestParam UUID userID
    ) throws Exception {
        return mediaService.updateMediaInfoById(id, mediaInfo, userID);
    }


    //ready
    /**
     * delete media by ID
     * @param id identifier of deleted media
     */
    @DeleteMapping("/media/{id}")
    @Operation(
            summary = "Delete a media",
            description = "Delete a media")
    @ApiResponse(responseCode = "204", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "422", description = "Empty ID")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    public void deleteMediaById(
            @Valid @PathVariable UUID id,
            @Valid @RequestParam UUID userID
    ) throws Exception {
        mediaService.eraseMedia(id, userID);
    }

    /*
    @GetMapping("/media-info-list")
    @Operation(
            summary = "Returns selected media info",
            description = "Returns selected media info")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "The database is empty")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Page<MediaInfoResponse>> getAlienCars(
            @Valid @RequestBody DataPage page,
            @Valid @RequestBody MediaSearchCriteria searchCriteria){
        return mediaService.mediaListCustomRead(page, searchCriteria);
    }
     */

}
