package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.dto.request.MediaInfoRequest;
import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.mapper.MediaMapper;
import com.education.project.media.holder.mediaholder.dto.response.paging.DataPage;
import com.education.project.media.holder.mediaholder.model.Media;
import com.education.project.media.holder.mediaholder.dto.response.paging.MediaSearchCriteria;
import com.education.project.media.holder.mediaholder.repository.MediaCriteriaRepository;
import com.education.project.media.holder.mediaholder.repository.MediaRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MediaServiceSecImp implements MediaServiceSec {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MediaCriteriaRepository mediaCriteriaRepository;
    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    @Qualifier("storageServiceImp")
    private StorageService storageService;

    @Autowired
    @Qualifier("permissionServiceImp")
    private PermissionService permission;

    @Override
    public ResponseEntity<Page<MediaInfoResponse>> mediaCustomListRead(
            @NotNull DataPage page,
            @NotNull MediaSearchCriteria searchCriteria,
            @NotNull UUID userId
    ) throws Exception {
        log.info("{\"get media info list\": {" +
                "\"page\": \"{}," +
                "\"search criteria\": \"{}" +
                "\"}}", page, searchCriteria);

        if(!permission.allowed(userId, Operation.GET_INFO))
            throw new Exception("ACCESS DENIED");

        return new ResponseEntity<>(
                mediaCriteriaRepository.findAllWithFilters(
                        page, searchCriteria),
                HttpStatus.OK
        );
    }


    @Override
    public ResponseEntity<MediaInfoResponse> createMedia(
            @NotNull MediaRequest mediaRequest,
            @NotNull UUID userId
    ) throws Exception {

        MultipartFile file = mediaRequest.fileBody();
        log.info("{\"add file\": {\"file size\": \"{}\"}}", file.getSize());

        if(!permission.allowed(userId, Operation.POST))
            throw new Exception("ACCESS DENIED");

        Media mediaResult = mediaRepository.save(
                mediaMapper.toMedia(
                        mediaRequest,
                        file.getOriginalFilename(),
                        file.getSize()));

        mediaResult.setFilePath(
                storageService.save(mediaResult.getId(), file).toString());

        return new ResponseEntity<>(
                mediaMapper.toDtoInfo(mediaRepository.save(mediaResult)),
                HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Resource> getMediaById(
            @NotNull UUID id,
            @NotNull UUID userId
    ) throws Exception {
        log.info("{\"return file\": {\"id\": {}}}", id);

        if(!permission.allowed(userId, Operation.GET))
            throw new Exception("ACCESS DENIED");

        Optional<Media> mediaOptional = mediaRepository.findById(id);
        if (mediaOptional.isEmpty()) throw new Exception("NOT FOUND");

        Media media = mediaOptional.get();
        return new ResponseEntity<>(
                storageService.load(media.getFilePath(), media.getFileName()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MediaInfoResponse> getMediaInfoById(
            @NotNull UUID id,
            @NotNull UUID userId
    ) throws Exception {
        log.info("{\"return file info\": {\"id\": {}}}", id);

        if(!permission.allowed(userId, Operation.GET_INFO))
            throw new Exception("ACCESS DENIED");

        Optional<Media> mediaOptional = mediaRepository.findById(id);
        if (mediaOptional.isEmpty()) throw new Exception("NOT FOUND");

        return new ResponseEntity<>(
                mediaMapper.toDtoInfo(mediaOptional.get()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MediaInfoResponse> updateMediaById(
            @NotNull UUID id,
            @NotNull MultipartFile file,
            @NotNull UUID userId
    ) throws Exception {
        log.info("{\"update file\": {\"id\": {}}}", id);

        if(!permission.allowed(userId, Operation.PUT))
            throw new Exception("ACCESS DENIED");

        String newFileName = file.getOriginalFilename();
        Long newFileSize = file.getSize();
        if (newFileName == null) throw new Exception("EMPTY FILE NAME");

        Optional<Media> mediaOptional = mediaRepository.findById(id);
        if (mediaOptional.isEmpty()) throw new Exception("NOT FOUND");
        Media media = mediaOptional.get();

        Path basePath = storageService.save(id, file);

        if (!Files.exists(basePath.resolve(newFileName)))
            throw new Exception("FILE NOT ACCEPTED");

        String oldFileName = media.getFileName();
        Long oldFileSize = media.getFileSize();

        media.setFileName(newFileName);
        media.setFileSize(newFileSize);
        mediaRepository.save(media);

        Files.delete(basePath.resolve(oldFileName));

        log.info("file size: old = {}, new = {}", oldFileSize, newFileSize);

        return new ResponseEntity<>(
                mediaMapper.toDtoInfo(media),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MediaInfoResponse> updateMediaInfoById(
            @NotNull UUID id,
            @NotNull MediaInfoRequest mediaInfo,
            @NotNull UUID userId
    ) throws Exception {
        log.info("{\"update file info\": {\"id\": {}}}", id);

        if(!permission.allowed(userId, Operation.PUT_INFO))
            throw new Exception("ACCESS DENIED");

        Optional<Media> mediaOptional = mediaRepository.findById(id);
        if (mediaOptional.isEmpty()) throw new Exception("NOT FOUND");

        Media media = mediaOptional.get();
        media.setName(mediaInfo.name());
        media.setDescription(mediaInfo.description());

        return new ResponseEntity<>(
                mediaMapper.toDtoInfo(mediaRepository.save(media)),
                HttpStatus.OK);
    }

    @Override
    public void eraseMedia(
            @NotNull UUID id,
            @NotNull UUID userId
    ) throws Exception {
        log.info("{\"delete file\": {\"id\": {}}}", id);

        if(!permission.allowed(userId, Operation.DELETE))
            throw new Exception("ACCESS DENIED");

        Optional<Media> mediaOptional = mediaRepository.findById(id);
        if (mediaOptional.isEmpty()) return;

        Media media = mediaOptional.get();
        if (media.getType() != -1) {
            media.setType(-1);
            mediaRepository.save(media);
        }

        if (storageService.delete(media.getFilePath(), media.getFileName())) {
            storageService.cleanPath(id);
            mediaRepository.deleteById(id);
        }
        throw new Exception("OPERATION SUCCESSFUL");
    }
}
