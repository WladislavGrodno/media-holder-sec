package com.education.project.media.holder.mediaholder.mapper;

import com.education.project.media.holder.mediaholder.dto.request.MediaRequest;
import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.model.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    @Mapping(source = "name", target = "fileName")
    @Mapping(source = "size", target = "fileSize")
    Media toMedia(Media media, String name, Long size);
    MediaInfoResponse toDtoInfo(Media media);
    List<MediaInfoResponse> toDtoInfo(List<Media> media);
    Media toMedia(MediaRequest request, String fileName, Long fileSize);

//  Page<MediaInfoResponse> toDtoInfo(Page<Media> media);

    /*
    @Mapping(target = "fileName",
            expression = "java(request.fileBody().getOriginalFilename())")
    @Mapping(target = "fileSize",
            expression = "java(request.fileBody().getSize())")
     */

    //@Mapping(source = "request.file.size", target = "file_size")
    //@Mapping(source = "request.file.originalFilename", target = "file_name")
    // @Mapping(source = "request.file.size", target = "file_size")
    //@Mapping(source = "request.type", target = "specialization")
    //@Mapping(source = "request.name", target = "specialization")

    /*
    default Media toMedia(MediaRequest request){
        MultipartFile file = request.fileBody();
        return new Media(
                null, null,
                request.name(), request.description(), request.type(),
                file.getOriginalFilename(), file.getSize(), ""
        );
    }
     */

}
