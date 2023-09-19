package com.education.project.media.holder.mediaholder.externals.user;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "java-user-service",
        url = "http://localhost:8100",
        path = ""

/*
        name = "item-service",
        url = "http://localhost:8100",
        path = "/user-items-service"

 */
)
public interface UserClient {
    @GetMapping(value = "/users/{userId}")
    ExternalUser getUser(@Valid @PathVariable UUID userId);
}
