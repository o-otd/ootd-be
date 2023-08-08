package com.ootd.be.entity;

import com.querydsl.core.types.dsl.BooleanExpression;
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

        BooleanExpression whereConditions = qComment.confirm.eq(confirm).and(qComment.depth.eq(0)).and(qComment.deleted.isFalse());
        JPAQuery<ConfirmComment> query = queryFactory.selectFrom(qComment)
                .where(whereConditions)
                .orderBy(qComment.rootComment.id.asc(), qComment.depth.asc(), qComment.createdAt.asc())
                .leftJoin(qComment.parentComment).fetchJoin();

        if (pageable != null) {
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }

        Long count = queryFactory.select(qComment.count())
                .from(qComment)
                .where(whereConditions)
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count.intValue());

    }

    public Page<ConfirmComment> findAllByComment(ConfirmComment comment, Pageable pageable) {

        BooleanExpression whereConditions = qComment.rootComment.eq(comment)
                .and(qComment.depth.ne(0))
                .and(qComment.deleted.isFalse())
                .and(qComment.ne(comment));

        JPAQuery<ConfirmComment> query = queryFactory.selectFrom(qComment)
                .where(whereConditions)
                .orderBy(qComment.createdAt.asc())
                .leftJoin(qComment.parentComment).fetchJoin();

        if (pageable != null) {
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }

        Long count = queryFactory.select(qComment.count())
                .from(qComment)
                .where(whereConditions)
                .fetchOne();

        return new PageImpl<>(query.fetch(), pageable, count.intValue());

    }



    public ConfirmComment best(Confirm confirm) {
        return queryFactory.selectFrom(qComment)
                .where(qComment.confirm.eq(confirm).and(qComment.deleted.isFalse()))
                .orderBy(qComment.likes.size().desc(), qComment.createdAt.desc())
                .limit(1L)
                .fetchOne();
    }

}
