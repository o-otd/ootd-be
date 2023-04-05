package com.ootd.be.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomConfirmCommentRepositoryImpl implements CustomConfirmCommentRepository {

    private static final com.ootd.be.entity.QConfirmComment qComment = com.ootd.be.entity.QConfirmComment.confirmComment;

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public List<ConfirmComment> findAllByConfirm(Confirm confirm) {

        List<ConfirmComment> comments = queryFactory.selectFrom(qComment)
                .where(qComment.confirm.eq(confirm))
                .orderBy(qComment.rootComment.id.asc(), qComment.depth.asc(), qComment.createdAt.asc())
                .join(qComment.parentComment).fetchJoin()
                .fetch();

        return comments;

    }

}
