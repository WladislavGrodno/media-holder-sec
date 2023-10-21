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

    @Mapping(source = "name", target = "fileName")
    @Mapping(source = "size", target = "fileSize")
    Media toMedia(MediaRequest request, String name, Long size);

    MediaInfoResponse toDtoInfo(Media media);

    List<MediaInfoResponse> toDtoInfo(List<Media> media);

}
