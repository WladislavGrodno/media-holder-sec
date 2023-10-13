package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.tools.PathChain;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceImpTest {
    @Mock
    private PathChain pathChain;
    @InjectMocks
    private StorageServiceImp storageServiceImp;

    private final Path storageRootPath =
            Paths.get("src/test/resources/media-storage").normalize();
    private final UUID mediaID = UUID.fromString("ecbe4167-9fac-4434-b135-8df2074ded29");
    private final String mediaPathString = storageRootPath + "/ecbe/4167/9fac/4434/b135/8df2/074d/ed29";



    @Test
    void save() throws Exception {

        MultipartFile file = new MockMultipartFile(
                "haha-ha.txt",
                "ha-ha-ha.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MultipartFile badFile = new MockMultipartFile(
                "haha-ha.txt",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        Path mediaPath = Path.of(mediaPathString);
        Path mediaFile = mediaPath.resolve(file.getOriginalFilename());
        if (Files.exists(mediaFile)) {
            Files.delete(mediaFile);
        }
        assertFalse(Files.exists(mediaFile));

        Mockito.when(pathChain.path(mediaID)).thenReturn(mediaPath);
        assertThrows(
                Exception.class,
                () -> storageServiceImp.save(mediaID, badFile),
                "EMPTY FILE NAME"
        );

        assertFalse(Files.exists(mediaFile));
        assertEquals(mediaPathString, storageServiceImp.save(mediaID, file).toString());
        assertTrue(Files.exists(mediaFile));

        if (Files.exists(mediaFile)) {
            Files.delete(mediaFile);
        }

    }

    @Test
    void load() throws Exception {
        String testString = "Hello, World!";
        MultipartFile multipartFile = new MockMultipartFile(
                "haha-ha.txt",
                "ha-ha-ha.txt",
                MediaType.TEXT_PLAIN_VALUE,
                testString.getBytes()
        );

        String fileName = multipartFile.getOriginalFilename();
        Path filePath = Path.of(mediaPathString);
        Path mediaPath = filePath.resolve(fileName);

        Mockito.when(pathChain.path(mediaPathString, "")).thenReturn(filePath);
        Mockito.when(pathChain.path(mediaPathString, fileName)).thenReturn(mediaPath);

        assertThrows(
                Exception.class,
                () -> storageServiceImp.load(mediaPathString, ""),
                "Could not find file"
        );

        String mediaFile = mediaPath.toString();
        multipartFile.transferTo(new File(mediaFile));
        assertEquals(
                FileUtils.readFileToString(new File(mediaFile), "UTF-8"),
                storageServiceImp.load(mediaPathString, fileName)
                        .getContentAsString(StandardCharsets.UTF_8)
        );
    }

    @Test
    void delete() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile(
                "haha-ha.txt",
                "ha-ha-ha.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        String fileName = multipartFile.getOriginalFilename();
        Path filePath = Path.of(mediaPathString);
        Path mediaPath = filePath.resolve(fileName);

        Mockito.when(pathChain.path(mediaPathString, fileName)).thenReturn(mediaPath);
        String mediaFile = mediaPath.toString();
        multipartFile.transferTo(new File(mediaFile));

        assertTrue(storageServiceImp.delete(mediaPathString, ""));
        assertTrue(storageServiceImp.delete(mediaPathString, fileName));
//        Path mediaPath = Path.of(mediaPathString);
//        Path file = mediaPath.resolve(fileName);
//        if (!Files.exists(file)) {
//            Files.createFile(file);
//            Files.writeString(file, "Hello, World!");
//        }
//



    }



    @Test
    void cleanPath() {
    }
}