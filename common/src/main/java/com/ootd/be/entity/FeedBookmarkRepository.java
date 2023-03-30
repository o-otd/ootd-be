package com.ootd.be.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBookmarkRepository extends JpaRepository<FeedBookmark, Long> {

    Optional<FeedBookmark> findByMemberAndFeed(Member member, Feed feed);

}
