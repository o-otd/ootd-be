package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RefreshToken extends BaseEntity {

    @Id @Column(name = "refresh_token_id")
    private Long id;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String refreshToken;

    private LocalDateTime expiredAt;

    private boolean expired;

}
