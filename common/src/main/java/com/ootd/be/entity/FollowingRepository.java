package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    Optional<Following> findByFollowerAndFollowee(Member follower, Member followee);

    List<Following> findByFollower(Member follower);
    long countByFollower(Member follower);
    List<Following> findByFollowee(Member followee);
    long countByFollowee(Member followee);

}
