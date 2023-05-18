package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmCommentLikeRepository extends JpaRepository<ConfirmCommentLike, Long> {

    Optional<ConfirmCommentLike> findByCommentAndMember(ConfirmComment comment, Member member);

}
