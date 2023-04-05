package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmVoteRepository extends JpaRepository<ConfirmVote, Long> {

    Optional<ConfirmVote> findByVoterAndConfirm(Member voter, Confirm confirm);


}
