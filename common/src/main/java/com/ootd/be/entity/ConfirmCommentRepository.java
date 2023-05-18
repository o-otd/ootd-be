package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmCommentRepository extends JpaRepository<ConfirmComment, Long>, CustomConfirmCommentRepository {

    Optional<ConfirmComment> findByParentComment(ConfirmComment parentComment);

    List<ConfirmComment> findAllByMember(Member member);

    long countByMember(Member member);

}
