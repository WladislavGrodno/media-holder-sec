package com.education.project.media.holder.mediaholder.integration.user;

import com.education.project.media.holder.mediaholder.integration.user.dto.ExternalUser;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "${integration.profile.userService.name}",
        url = "${integration.profile.userService.url}",
        path = "${integration.profile.userService.context-path}"
)
public interface UserClient {
    @GetMapping(value = "/users/{userId}")
    ExternalUser getUser(@Valid @PathVariable UUID userId);
}
