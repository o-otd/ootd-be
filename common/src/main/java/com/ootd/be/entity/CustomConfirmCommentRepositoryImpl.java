package com.ootd.be.entity;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomConfirmCommentRepositoryImpl implements CustomConfirmCommentRepository {

    private static final com.ootd.be.entity.QConfirmComment qComment = com.ootd.be.entity.QConfirmComment.confirmComment;

    private final JPAQueryFactory queryFactory;

    public Page<ConfirmComment> findAllByConfirm(Confirm confirm) {
        return findAllByConfirm(confirm, null);
    }

    public Page<ConfirmComment> findAllByConfirm(Confirm confirm, Pageable pageable) {

        JPAQuery<ConfirmComment> query = queryFactory.selectFrom(qComment)
                .where(qComment.confirm.eq(confirm).and(qComment.depth.eq(0)))
                .orderBy(qComment.rootComment.id.asc(), qComment.depth.asc(), qComment.createdAt.asc())
                .leftJoin(qComment.parentComment).fetchJoin();

        if (pageable != null) {
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }

        Long count = queryFactory.select(qComment.count())
                .from(qComment)
                .where(qComment.confirm.eq(confirm).and(qComment.depth.eq(0)))
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count.intValue());

    }

    public Page<ConfirmComment> findAllByComment(ConfirmComment comment, Pageable pageable) {

        JPAQuery<ConfirmComment> query = queryFactory.selectFrom(qComment)
                .where(qComment.parentComment.eq(comment))
                .orderBy(qComment.createdAt.asc())
                .leftJoin(qComment.parentComment).fetchJoin();

        if (pageable != null) {
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }

        Long count = queryFactory.select(qComment.count())
                .from(qComment)
                .where(qComment.parentComment.eq(comment))
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count.intValue());

    }



    public ConfirmComment best(Confirm confirm) {
        return queryFactory.selectFrom(qComment)
                .where(qComment.confirm.eq(confirm))
                .orderBy(qComment.likes.size().desc(), qComment.createdAt.desc())
                .limit(1L)
                .fetchOne();
    }

}
