package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmVoteTypeRepository extends JpaRepository<ConfirmVoteType, Long> {

    List<ConfirmVoteType> findByConfirm(Confirm confirm);

}
