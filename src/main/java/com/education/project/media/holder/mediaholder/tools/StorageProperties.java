package com.education.project.media.holder.mediaholder.tools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
    private String location;
}
