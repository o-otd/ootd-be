package com.ootd.be.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Feed extends BaseEntity {

    @Id @Column(name = "feed_id")
    private Long id;

    private String title;
    private String contents;

    private String mainImage;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST)
    private List<FeedItem> items;

    @OneToMany(mappedBy = "feed", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<FeedFilter> feedFilters;

    @OneToMany(mappedBy = "feed", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<FeedHashTag> feedHashTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "feed")
    private List<FeedLike> feedLikes;

    @OneToMany(mappedBy = "feed")
    private List<FeedBookmark> feedBookmarks;

}
