package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.tools.PathChain;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageServiceImp implements StorageService {
    private final Path storageRootPath;
    private final PathChain pathChain;

    //@Autowired
    //StorageConfiguration storageConfiguration;
    //StorageProperties storageProperties;// = new StorageProperties();

    public StorageServiceImp() {
        this.storageRootPath = Paths.get(
                //storageProperties.getLocation()
                "./media-storage"
        ).normalize();
        pathChain = new PathChain(storageRootPath);
    }

    /*
    @Override
    @PostConstruct
    public void init() throws IOException {
        if (!Files.exists(storageRootPath)) {
                Files.createDirectories(storageRootPath);
                log.info("Storage {} was created", storageRootPath);
        }
        else log.info("Storage already exist");
    }
     */

    @Override
    public Path save(UUID id, MultipartFile file) throws Exception {
        Path path = pathChain.path(id);
        String name = file.getOriginalFilename();
        if (name == null) throw new Exception("EMPTY FILE NAME");

        Files.copy(
                file.getInputStream(),
                path.resolve(name),
                StandardCopyOption.REPLACE_EXISTING);
        return path;
    }

    @Override
    public Resource load(String filePath,
                         String fileName) throws Exception {
        Path file = pathChain.path(filePath, fileName);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) return resource;
        throw new Exception("Could not find file");
    }

    @Override
    public boolean delete(String filePath,
                          String fileName) throws IOException {
        Path path = pathChain.path(filePath, fileName);
        if (Files.exists(path)) Files.delete(path);
        return !Files.exists(path);
    }

    @Override
    public void cleanPath(UUID id) throws IOException {
        pathChain.cleanPath(id);
    }
}
