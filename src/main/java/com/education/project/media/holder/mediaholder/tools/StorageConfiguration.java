package com.education.project.media.holder.mediaholder.tools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfiguration {
    @Autowired
    private StorageProperties storageProperties;
}
