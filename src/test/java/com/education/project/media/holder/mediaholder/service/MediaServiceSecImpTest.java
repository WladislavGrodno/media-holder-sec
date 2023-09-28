package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.mapper.MediaMapper;
import com.education.project.media.holder.mediaholder.model.Media;
import com.education.project.media.holder.mediaholder.repository.MediaRepository;
import com.education.project.media.holder.mediaholder.tools.PathChain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceSecImpTest {

    @Mock
    private PathChain pathChain;
    @Mock
    private MediaRepository mediaRepository;
//    @Mock
//    private MediaCriteriaRepository mediaCriteriaRepository;
    @Mock
    private MediaMapper mediaMapper;
    @Mock
    private StorageService storageService;
    @Mock
    private PermissionService permission;

    @InjectMocks
    private MediaServiceSecImp mediaServiceSecImp;

    private final Path storageRootPath =
            Paths.get("src/test/resources/media-storage").normalize();
    private final UUID mediaID = UUID.fromString("ecbe4167-9fac-4434-b135-8df2074ded29");

    private final String mediaPathString =
            storageRootPath + "/ecbe/4167/9fac/4434/b135/8df2/074d/ed29";
    private final UUID userID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fceba");
    private final UUID adminID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebb");
    private final UUID anonymousID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebc");


    private final String fileName = "haha.txt";

    private final MediaRequest mediaRequest = new MediaRequest(
            "Test name",
            "Test description",
            1,
            new MockMultipartFile(
                    "haha-ha.txt",
                    fileName,
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World!".getBytes()
            )
    );

    private final Instant instantNow = Instant.now();


    private final MediaInfoResponse mediaInfoResponse = new MediaInfoResponse(mediaID,
            instantNow,
            "Test name",
            "Test description",
            1,
            fileName,
            13L);

    private final Media newMedia = new Media(
            mediaID,
            instantNow,
            "Test name",
            "Test description",
            1,
            fileName,
            13L,
            ""
    );

    private final Media savedMedia = new Media(
            mediaID,
            instantNow,
            "Test name",
            "Test description",
            1,
            fileName,
            13L,
            mediaPathString
    );

    @Test
    void getMediaById() throws Exception{

        Mockito.when(permission.allowed(userID, Operation.GET)).thenReturn(true);
        Mockito.when(permission.allowed(anonymousID, Operation.GET)).thenReturn(false);
//        Mockito.when(permission.allowed(adminID, Operation.GET_INFO)).thenReturn(true);
//        Mockito.when(permission.allowed(anonymousID, Operation.GET_INFO)).thenReturn(true);

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.getMediaById(mediaID, anonymousID),
                "ACCESS DENIED");

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));
        Mockito.when(mediaRepository.findById(userID)).thenReturn(Optional.empty());

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.getMediaById(userID, userID),
                "NOT FOUND");

        Resource resource = new UrlResource(Paths
                .get(savedMedia.getFilePath())
                .resolve(savedMedia.getFileName()).toUri());

        Mockito.when(storageService.load(savedMedia.getFilePath(), savedMedia.getFileName()))
                .thenReturn(new UrlResource(Paths.get(savedMedia.getFilePath())
                        .resolve(savedMedia.getFileName()).toUri()));

        assertEquals(
                new ResponseEntity<>(resource,HttpStatus.OK),
                mediaServiceSecImp.getMediaById(mediaID, userID));



    }

    @Test
    void getMediaInfoById() throws Exception{

        Mockito.when(permission.allowed(userID, Operation.GET_INFO)).thenReturn(true);
        Mockito.when(permission.allowed(anonymousID, Operation.GET_INFO)).thenReturn(false);
//        Mockito.when(permission.allowed(adminID, Operation.GET_INFO)).thenReturn(true);
//        Mockito.when(permission.allowed(anonymousID, Operation.GET_INFO)).thenReturn(true);

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.getMediaInfoById(mediaID, anonymousID),
                "ACCESS DENIED");

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.getMediaInfoById(mediaID, userID),
                "NOT FOUND");

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));
        Mockito.when(mediaMapper.toDtoInfo(savedMedia)).thenReturn(mediaInfoResponse);
        assertEquals(new ResponseEntity<>(mediaInfoResponse,HttpStatus.OK),
                mediaServiceSecImp.getMediaInfoById(mediaID, userID));

    }

    @Test
    void createMedia() throws Exception {

        Mockito.when(permission.allowed(userID, Operation.POST)).thenReturn(true);
        Mockito.when(permission.allowed(anonymousID, Operation.POST)).thenReturn(false);

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.createMedia(mediaRequest, anonymousID),
                "ACCESS DENIED");

        MultipartFile file = mediaRequest.fileBody();

        Mockito.when(mediaRepository.save(
                mediaMapper.toMedia(
                        mediaRequest,
                        file.getOriginalFilename(),
                        file.getSize())))
                .thenReturn(newMedia);

        Mockito.when(storageService.save(savedMedia.getId(), file))
                .thenReturn(Path.of(savedMedia.getFilePath()));

        Mockito.when(mediaRepository.save(newMedia)).thenReturn(savedMedia);
        Mockito.when(mediaMapper.toDtoInfo(savedMedia)).thenReturn(mediaInfoResponse);

        assertEquals(new ResponseEntity<>(mediaInfoResponse,HttpStatus.OK),
                mediaServiceSecImp.createMedia(mediaRequest, userID));

    }

    @Test
    void eraseMedia() throws Exception {

        Mockito.when(permission.allowed(userID, Operation.DELETE)).thenReturn(false);
        Mockito.when(permission.allowed(anonymousID, Operation.DELETE)).thenReturn(false);
        Mockito.when(permission.allowed(adminID, Operation.DELETE)).thenReturn(true);

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, anonymousID),
                "ACCESS DENIED");
        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, userID),
                "ACCESS DENIED");

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, adminID),
                "OPERATION SUCCESSFUL");

        Mockito.when(storageService.delete(savedMedia.getFilePath(), savedMedia.getFileName())).thenReturn(true);
        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, adminID),
                "OPERATION SUCCESSFUL");

    }

    /*
    @Test
    void updateMediaInfoById() throws Exception{
        Mockito.when(permission.allowed(userID, Operation.PUT_INFO)).thenReturn(false);
        Mockito.when(permission.allowed(anonymousID, Operation.PUT_INFO)).thenReturn(false);
        Mockito.when(permission.allowed(adminID, Operation.PUT_INFO)).thenReturn(true);

        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.updateMediaInfoById(mediaID, anonymousID),
                "ACCESS DENIED");
        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.updateMediaInfoById(mediaID, userID),
                "ACCESS DENIED");
    }
     */

}