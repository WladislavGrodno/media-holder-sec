package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.dto.request.MediaInfoRequest;
import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.dto.response.paging.DataPage;
import com.education.project.media.holder.mediaholder.dto.response.paging.MediaSearchCriteria;
import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.exception.ExceptionAccessDenied;
import com.education.project.media.holder.mediaholder.exception.ExceptionNotFound;
import com.education.project.media.holder.mediaholder.exception.ExceptionOperationSuccessful;
import com.education.project.media.holder.mediaholder.mapper.MediaMapper;
import com.education.project.media.holder.mediaholder.model.Media;
import com.education.project.media.holder.mediaholder.repository.MediaCriteriaRepository;
import com.education.project.media.holder.mediaholder.repository.MediaRepository;
import com.education.project.media.holder.mediaholder.tools.PathChain;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
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
    @Mock
    private MediaCriteriaRepository mediaCriteriaRepository;
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
    private final String mediaPathString = storageRootPath + "/ecbe/4167/9fac/4434/b135/8df2/074d/ed29";
    private final UUID userID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fceba");
    private final UUID adminID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebb");
    private final UUID anonymousID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebc");


    private final String fileName = "haha.txt";
    private final String newFileName = "hoho.txt";

    @BeforeEach
    void setup() throws IOException {
        Path mediaPath = Path.of(mediaPathString);
        if (Files.exists(mediaPath.resolve(fileName)))
            Files.delete(mediaPath.resolve(fileName));
        if (Files.exists(mediaPath.resolve(newFileName)))
            Files.delete(mediaPath.resolve(newFileName));
    }

    private final MediaRequest mediaRequest = new MediaRequest(
            "Test name",
            "Test description",
            1,
            new MockMultipartFile(
                    "haha-ha.txt",
                    fileName,
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World!".getBytes())
    );

    /*
    private final MultipartFile file =  new MockMultipartFile(
            "haha-ha.txt",
            fileName,
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes());


     */
    private final Instant instantNow = Instant.now();


    private final MediaInfoResponse mediaInfoResponse = new MediaInfoResponse(mediaID,
            instantNow,
            "Test name",
            "Test description",
            1,
            fileName,
            13L);


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
                ExceptionAccessDenied.class,
                ()->mediaServiceSecImp.getMediaById(mediaID, anonymousID));

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));
        Mockito.when(mediaRepository.findById(userID)).thenReturn(Optional.empty());

        assertThrows(
                ExceptionNotFound.class,
                ()->mediaServiceSecImp.getMediaById(userID, userID));

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
                ExceptionAccessDenied.class,
                ()->mediaServiceSecImp.getMediaInfoById(mediaID, anonymousID));

        assertThrows(
                ExceptionNotFound.class,
                ()->mediaServiceSecImp.getMediaInfoById(mediaID, userID));

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
                ExceptionAccessDenied.class,
                ()->mediaServiceSecImp.createMedia(mediaRequest, anonymousID));
                //()->mediaServiceSecImp.createMedia(mediaRequest, file, anonymousID));

        MultipartFile file = mediaRequest.fileBody();

        Media newMedia = new Media(
                mediaID,
                instantNow,
                "Test name",
                "Test description",
                1,
                fileName,
                13L,
                ""
        );

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
        //      mediaServiceSecImp.createMedia(mediaRequest, file, userID));
    }

    @Test
    void eraseMedia() throws Exception {

        Mockito.when(permission.allowed(userID, Operation.DELETE)).thenReturn(false);
        Mockito.when(permission.allowed(anonymousID, Operation.DELETE)).thenReturn(false);
        Mockito.when(permission.allowed(adminID, Operation.DELETE)).thenReturn(true);

        assertThrows(
                ExceptionAccessDenied.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, anonymousID));
        assertThrows(
                ExceptionAccessDenied.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, userID));

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));
        Mockito.when(mediaRepository.findById(userID)).thenReturn(Optional.empty());

        assertThrows(
                ExceptionOperationSuccessful.class,
                ()->mediaServiceSecImp.eraseMedia(userID, adminID));
        assertThrows(
                ExceptionOperationSuccessful.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, adminID));

        Mockito.when(storageService.delete(savedMedia.getFilePath(), savedMedia.getFileName())).thenReturn(true);
        assertThrows(
                Exception.class,
                ()->mediaServiceSecImp.eraseMedia(mediaID, adminID),
                "OPERATION SUCCESSFUL");
    }

    @Test
    void updateMediaInfoById() throws Exception{
        Mockito.when(permission.allowed(userID, Operation.PUT_INFO)).thenReturn(true);
        Mockito.when(permission.allowed(anonymousID, Operation.PUT_INFO)).thenReturn(false);

        MediaInfoRequest mediaInfoRequest = new MediaInfoRequest(
                "Updated test name",
                "Updated test description"
        );

        assertThrows(
                ExceptionAccessDenied.class,
                ()->mediaServiceSecImp.updateMediaInfoById(mediaID, mediaInfoRequest, anonymousID));

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));
        Mockito.when(mediaRepository.findById(userID)).thenReturn(Optional.empty());

        assertThrows(
                ExceptionNotFound.class,
                ()->mediaServiceSecImp.updateMediaInfoById(userID, mediaInfoRequest, userID));

        Media updatedMedia = new Media(
                mediaID,
                instantNow,
                "Updated test name",
                "Updated test description",
                1,
                fileName,
                13L,
                mediaPathString
        );

        Mockito.when(mediaRepository.save(savedMedia)).thenReturn(updatedMedia);

        MediaInfoResponse updatedMediaInfoResponse = new MediaInfoResponse(mediaID,
                instantNow,
                "Updated test name",
                "Updated test description",
                1,
                fileName,
                13L);

        Mockito.when(mediaMapper.toDtoInfo(updatedMedia)).thenReturn(updatedMediaInfoResponse);

        assertEquals(new ResponseEntity<>(updatedMediaInfoResponse,HttpStatus.OK),
                mediaServiceSecImp.updateMediaInfoById(mediaID, mediaInfoRequest, userID));
    }

    @Test
    void updateMediaById() throws Exception {
        Mockito.when(permission.allowed(userID, Operation.PUT)).thenReturn(true);
        Mockito.when(permission.allowed(anonymousID, Operation.PUT)).thenReturn(false);

        MultipartFile multipartFile = new MockMultipartFile(
                "hoho-ho.txt",
                "hoho.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, Life!".getBytes()
        );
        assertThrows(
                ExceptionAccessDenied.class,
                () -> mediaServiceSecImp.updateMediaById(
                        mediaID, multipartFile, anonymousID));

        MultipartFile wrongMultipartFile = new MockMultipartFile(
                "hoho-ho.txt",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        assertThrows(
                Exception.class,
                () -> mediaServiceSecImp.updateMediaById(
                        mediaID, wrongMultipartFile, userID),
                "EMPTY FILE NAME");

        Path mediaPath = Path.of(mediaPathString);
        Path file = mediaPath.resolve(fileName);
        if (!Files.exists(file)) {
            Files.createFile(file);
            Files.writeString(file, "Hello, World!");
        }

        Mockito.when(mediaRepository.findById(userID)).thenReturn(Optional.empty());
        assertThrows(
                ExceptionNotFound.class,
                () -> mediaServiceSecImp.updateMediaById(
                        userID, multipartFile, userID));

        Mockito.when(mediaRepository.findById(mediaID)).thenReturn(Optional.of(savedMedia));
        Mockito.when(storageService.save(savedMedia.getId(), multipartFile))
                .thenReturn(Path.of(savedMedia.getFilePath()));

        assertThrows(
                Exception.class,
                () -> mediaServiceSecImp.updateMediaById(
                        mediaID, multipartFile, userID),
                "FILE NOT ACCEPTED"
        );

        assertFalse(Files.exists(mediaPath.resolve(newFileName)));
        assertTrue(Files.exists(mediaPath.resolve(fileName)));


        Path newFile = mediaPath.resolve(newFileName);
        if (!Files.exists(newFile)) {
            Files.createFile(newFile);
            Files.writeString(newFile, "Hello, Life!");
        }

        Media updatedMedia = new Media(
                mediaID,
                instantNow,
                "Test name",
                "Test description",
                1,
                newFileName,
                12L,
                mediaPathString
        );

        MediaInfoResponse updatedMediaInfoResponse = new MediaInfoResponse(
                mediaID,
                instantNow,
                "Test name",
                "Test description",
                1,
                newFileName,
                12L);


        Mockito.when(mediaRepository.save(savedMedia)).thenReturn(updatedMedia);
        Mockito.when(mediaMapper.toDtoInfo(updatedMedia)).thenReturn(updatedMediaInfoResponse);

        assertEquals(
                new ResponseEntity<>(new MediaInfoResponse(mediaID, instantNow, "Test name", "Test description", 1, newFileName, 12L), HttpStatus.OK),
                mediaServiceSecImp.updateMediaById(mediaID, multipartFile, userID));

        assertTrue(Files.exists(mediaPath.resolve(newFileName)));
        assertFalse(Files.exists(mediaPath.resolve(fileName)));
    }

/*
    @Test
    void mediaCustomListRead() throws Exception {
        Mockito.when(permission.allowed(adminID, Operation.GET_INFO_LIST)).thenReturn(true);
        Mockito.when(permission.allowed(anonymousID, Operation.GET_INFO_LIST)).thenReturn(false);

        DataPage page = new DataPage();
        MediaSearchCriteria searchCriteria = new MediaSearchCriteria();


        assertThrows(
                ExceptionAccessDenied.class,
                () -> mediaServiceSecImp.mediaInfoCustomListRead(
                        page, searchCriteria, anonymousID));




        Mockito.when(mediaCriteriaRepository.findAllWithFilters(page, searchCriteria))
                .thenReturn();

        TypedQuery<Media> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());
        typedQuery.setMaxResults(page.getPageSize());


        Pageable pageable = PageRequest.of(
                page.getPageNumber(),
                page.getPageSize(),
                Sort.by(page.getSortDirection(), page.getSortBy()));

        return new PageImpl<>(
                mediaMapper.toDtoInfo(typedQuery.getResultList()),
                pageable,
                mediasCount);


        assertEquals(
                new ResponseEntity<>(new ,
                mediaServiceSecImp.mediaInfoCustomListRead(page, searchCriteria, adminID));



//        assertThrows(
//                ExceptionAccessDenied.class,
//                () -> mediaServiceSecImp.mediaInfoCustomListRead(
//                        page, searchCriteria, adminID));
//

    }
 */

}