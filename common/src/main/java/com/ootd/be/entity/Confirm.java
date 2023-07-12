package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Confirm extends BaseEntity {

    @Id
    @Column(name = "confirm_id")
    private Long id;

    private String content;

    private String startDate;
    private String endDate;

    @ToString.Exclude
    @OneToMany(mappedBy = "confirm", cascade = {CascadeType.PERSIST})
    private List<ConfirmImage> images;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ToString.Exclude
    @OneToMany(mappedBy = "confirm")
    private List<ConfirmComment> comments;

    @ToString.Exclude
    @OneToMany(mappedBy = "confirm", cascade = {CascadeType.PERSIST})
    private List<ConfirmVoteType> voteTypes;

    @ToString.Exclude
    @OneToMany(mappedBy = "confirm")
    private List<ConfirmVote> votes;

    private boolean completed;

}
