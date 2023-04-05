package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Following extends BaseEntity {

    @Id @Column(name = "following_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "member_id")
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", referencedColumnName = "member_id")
    private Member followee;

}
