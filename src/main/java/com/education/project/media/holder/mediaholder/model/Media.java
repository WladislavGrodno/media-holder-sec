package com.education.project.media.holder.mediaholder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", insertable = false, updatable = false)
    private UUID id;

    @Column(name = "upload_time", insertable = false, updatable = false)
    @CreationTimestamp
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Instant uploadTime;

    @Column(name = "name", nullable = false)
    private String name = "";

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "file_name", nullable = false)
    private String fileName = "";

    @Column(name = "file_size", nullable = false)
    private Long fileSize = 0L;

    @Column(name = "file_path", nullable = false, insertable = false)
    private String filePath = "";

    //public enum MediaType {IMG, AUD, VID}
}
