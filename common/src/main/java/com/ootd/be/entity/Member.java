package com.ootd.be.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
public class Member {

    @Id @Column(name = "member_id")
    private Long id;

    private String email;
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Exclude
    @OneToMany(mappedBy = "member")
    private List<Feed> feeds;

    @Exclude
    @OneToMany(mappedBy = "member")
    private List<FeedLike> feedLikes;

    @Exclude
    @OneToMany(mappedBy = "member")
    private List<ProductLike> productLikes;

    @Exclude
    @OneToMany(mappedBy = "member")
    private List<FeedBookmark> feedBookmarks;




}
