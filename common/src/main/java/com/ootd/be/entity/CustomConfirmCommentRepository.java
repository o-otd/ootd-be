package com.ootd.be.entity;

import java.util.List;

public interface CustomConfirmCommentRepository {

    List<ConfirmComment> findAllByConfirm(Confirm confirm);

}
