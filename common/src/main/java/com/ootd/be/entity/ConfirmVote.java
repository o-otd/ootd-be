package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class ConfirmVote extends BaseEntity {

    @Id @Column(name = "confirm_vote_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_id")
    private Confirm confirm;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_vote_type_id")
    private ConfirmVoteType voteType;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", referencedColumnName = "member_id")
    private Member voter;

}
