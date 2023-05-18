package com.ootd.be.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmImageRepository extends JpaRepository<ConfirmImage, Long> {

}
