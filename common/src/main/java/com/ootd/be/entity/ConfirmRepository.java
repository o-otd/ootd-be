package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, Long> {


    List<Confirm> findAllByMember(Member member);
    long countByMember(Member member);

}
