package com.education.project.media.holder.mediaholder.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface StorageService {

    Path save(@NotNull UUID id,
              @NotNull MultipartFile file) throws Exception;

    Resource load(
            @NotNull String filePath,
            @NotNull String fileName) throws Exception;

    boolean delete(
            @NotNull String filePath,
            @NotNull String fileName) throws IOException;

    void cleanPath(UUID id) throws IOException;

    //Stream<Path> loadAll();
    //void deleteAll();
    //void init() throws IOException;
}
