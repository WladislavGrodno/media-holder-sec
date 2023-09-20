package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.mapper.MediaMapper;
import com.education.project.media.holder.mediaholder.repository.MediaCriteriaRepository;
import com.education.project.media.holder.mediaholder.repository.MediaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceSecImpTest {


    @Mock
    private MediaRepository mediaRepository;
    @Mock
    private MediaCriteriaRepository mediaCriteriaRepository;
    @Mock
    private MediaMapper mediaMapper;

    @Mock
    private StorageService storageService;

//    @Autowired
//    @Qualifier("permissionServiceImp")
    @Mock
    private PermissionService permission;

    @InjectMocks
    private MediaServiceSecImp mediaServiceSecImp;

    private UUID testMediaID = UUID.fromString("1c7c17e0-39b5-4d8a-b036-fd263b699733");
    private UUID userID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fceba");
    private UUID adminID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebb");
    private UUID anonymousID = UUID.fromString("70c4495e-cc8c-494b-bbc7-7d04b97fcebc");

    @Test
    void getMediaInfoById() throws Exception{

        Mockito.when(permission.allowed(userID, Operation.GET_INFO)).thenReturn(true);
//        Mockito.when(permission.allowed(adminID, Operation.GET_INFO)).thenReturn(true);
//        Mockito.when(permission.allowed(anonymousID, Operation.GET_INFO)).thenReturn(true);

assertThrows(Exception.class, ()->mediaServiceSecImp.getMediaInfoById(testMediaID, userID), "NOT FOUND");




//        assertEquals("Player attack with: Sword",
//                mediaServiceSecImp.getMediaInfoById(testMediaID, userID));
        //ResponseEntity<MediaInfoResponse> responseE =


    }
}