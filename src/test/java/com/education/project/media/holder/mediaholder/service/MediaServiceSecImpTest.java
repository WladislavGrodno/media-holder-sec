package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.mapper.MediaMapper;
import com.education.project.media.holder.mediaholder.repository.MediaCriteriaRepository;
import com.education.project.media.holder.mediaholder.repository.MediaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

//import static org.junit.jupiter.api.Assertions.*;

class MediaServiceSecImpTest {


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

    @Test
    void createMedia() {
    }
}