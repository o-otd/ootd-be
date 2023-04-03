package com.ootd.be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    private Long id;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String refreshToken;

    private LocalDateTime expiredAt;

    private boolean expired;

}
