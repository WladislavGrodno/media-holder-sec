package com.education.project.media.holder.mediaholder.service;

import com.education.project.media.holder.mediaholder.externals.user.ExternalUser;
import com.education.project.media.holder.mediaholder.externals.user.UserClient;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PermissionServiceImp implements PermissionService{
    @Autowired
    private UserClient userClient;

    @Override
    public boolean allowed(
            @NotNull UUID userID,
            @NotNull Operation operation){

        switch (operation){
            case DELETE -> {return getRole(userID) == Role.ADMINISTRATOR;}
            case GET, GET_INFO -> {return true;}
            default -> {
                Role role = getRole(userID);
                return role != Role.ANONYMOUS && role != Role.UNKNOWN;}
        }
    }
    private Role getRole(@NotNull UUID userID){
        try{
            ExternalUser user =  userClient.getUser(userID);
            if (user == null) return Role.UNKNOWN;
            switch (user.role().roleDescr()){
                case "system admin" ->{return Role.ADMINISTRATOR;}
                case "moderator" -> {return Role.MODERATOR;}
                case "user" -> {return Role.USER;}
                case "anonymous" -> {return Role.ANONYMOUS;}
                default -> {return Role.UNKNOWN;}
            }
        }
        catch (Exception e){
            return Role.UNKNOWN;
        }
    }
}
