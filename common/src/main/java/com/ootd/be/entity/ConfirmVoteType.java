package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class ConfirmVoteType extends BaseEntity {

    @Id
    @Column(name = "confirm_vote_type_id")
    private Long id;

    private String wording;

    @Column(name = "dispaly_order")
    private int order;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_id")
    private Confirm confirm;

    @ToString.Exclude
    @OneToMany(mappedBy = "voteType")
    private List<ConfirmVote> confirmVotes;

}
