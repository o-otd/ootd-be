package com.ootd.be.entity;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

    private Long creator;
    private LocalDateTime createdAt;

    private Long updater;
    private LocalDateTime updatedAt;

    private boolean deleted;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
