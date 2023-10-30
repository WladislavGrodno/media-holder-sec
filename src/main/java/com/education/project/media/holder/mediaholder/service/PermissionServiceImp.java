package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.enums.Operation;
import com.education.project.media.holder.mediaholder.enums.Role;
import com.education.project.media.holder.mediaholder.integration.user.UserClient;
import com.education.project.media.holder.mediaholder.mapper.UserMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PermissionServiceImp implements PermissionService{
    @Autowired
    private UserClient userClient;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean allowed(
            @NotNull UUID userID,
            @NotNull Operation operation){
        return switch (operation) {
            case DELETE, GET_INFO_LIST -> getRole(userID) == Role.ADMINISTRATOR;
            case GET, GET_INFO -> true;
            default -> {
                Role role = getRole(userID);
                yield role != Role.ANONYMOUS && role != Role.UNKNOWN;
            }
        };
    }

    private Role getRole(@NotNull UUID userID){
        try{
            return userMapper.toRole(userClient.getUser(userID));
        }
        catch (Exception e){
            return Role.UNKNOWN;
        }
    }
}
