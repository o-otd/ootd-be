package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ConfirmVote extends BaseEntity {

    @Id @Column(name = "confirm_vote_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConfirmVoteType voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_id")
    private Confirm confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", referencedColumnName = "member_id")
    private Member voter;

}
