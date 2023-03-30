package com.ootd.be.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Feed {

    @Id
    private Long id;

    private String title;
    private String contents;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Feeditem> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "feed")
    private List<FeedLike> feedLikes;

    @OneToMany(mappedBy = "feed")
    private List<FeedBookmark> feedBookmarks;

}
