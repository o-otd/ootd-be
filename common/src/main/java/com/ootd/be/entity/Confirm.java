package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToMany(mappedBy = "confirm", cascade = {CascadeType.PERSIST})
    private List<ConfirmImage> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "confirm")
    private List<ConfirmComment> comments;

    private boolean completed;



}
