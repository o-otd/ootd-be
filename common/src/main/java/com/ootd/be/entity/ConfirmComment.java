package com.ootd.be.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ConfirmComment extends BaseEntity {

    @Id @Column(name = "confirm_comment_id")
    private Long id;

    private int depth;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_comment_id", referencedColumnName = "confirm_comment_id")
    private ConfirmComment rootComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "confirm_comment_id")
    private ConfirmComment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirm_id")
    private Confirm confirm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean deleted;

    public void addTo(ConfirmComment parentComment) {
        if (parentComment == null) return;

        setParentComment(parentComment);
        setRootComment(parentComment.getRootComment());

        setDepth(1);
    }

}
