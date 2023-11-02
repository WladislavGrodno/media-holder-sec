package com.education.project.media.holder.mediaholder.mapper;

import com.education.project.media.holder.mediaholder.enums.Role;
import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default Role toRole(ExternalUser user){
        if (user == null) return Role.UNKNOWN;
        return switch (user.roleDtoResp().roleDescr()){
            case "system admin" -> Role.ADMINISTRATOR;
            case "moderator" -> Role.MODERATOR;
            case "user" -> Role.USER;
            case "anonymous" -> Role.ANONYMOUS;
            default -> Role.UNKNOWN;
        };
    }
}
