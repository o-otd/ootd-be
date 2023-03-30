package com.ootd.be.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
public class Member {

    @Id
    private Long id;

    private String email;
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Exclude
    @OneToMany
    private List<Feed> feeds;

    @Exclude
    @OneToMany(mappedBy = "member")
    private List<FeedLike> feedLikes;

    @Exclude
    @OneToMany(mappedBy = "member")
    private List<FeedBookmark> feedBookmarks;


}
