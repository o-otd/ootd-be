package com.ootd.be.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OotdSeq {

    @Id @Column(name = "ootd_seq_id")
    private Long id;

    private Long seq;
    private LocalDateTime updatedAt;

}
