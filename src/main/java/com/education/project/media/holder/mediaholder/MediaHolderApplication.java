package com.education.project.media.holder.mediaholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MediaHolderApplication {
	public static void main(String[] args) {
		SpringApplication.run(MediaHolderApplication.class, args);
	}
}
