package com.ootd.be.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    Optional<FeedLike> findByMemberAndFeed(Member member, Feed feed);

    List<FeedLike> findByMember(Member member);
    long countByMember(Member member);

}
