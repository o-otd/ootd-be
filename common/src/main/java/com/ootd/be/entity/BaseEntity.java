package com.ootd.be.entity;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.springframework.data.domain.Persistable;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Persistable {

    private Long creator;
    private LocalDateTime createdAt;

    private Long updater;
    private LocalDateTime updatedAt;

    private transient boolean isNew;

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
