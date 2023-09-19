package com.education.project.media.holder.mediaholder.repository;

import com.education.project.media.holder.mediaholder.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<Media, UUID> {
}
