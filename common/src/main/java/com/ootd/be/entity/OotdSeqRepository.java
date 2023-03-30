package com.ootd.be.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

@Repository
public interface OotdSeqRepository extends JpaRepository<OotdSeq, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<OotdSeq> findById(Long id);

}
