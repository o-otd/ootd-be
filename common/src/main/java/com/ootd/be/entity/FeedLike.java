package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FeedLike extends BaseEntity {

    @Id @Column(name = "feed_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

}
