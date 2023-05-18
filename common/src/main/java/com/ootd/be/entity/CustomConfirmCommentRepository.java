package com.ootd.be.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomConfirmCommentRepository {

    Page<ConfirmComment> findAllByConfirm(Confirm confirm);

    Page<ConfirmComment> findAllByConfirm(Confirm confirm, Pageable pageable);

    ConfirmComment best(Confirm confirm);

}
