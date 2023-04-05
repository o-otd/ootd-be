package com.ootd.be.api.confirm;

import com.ootd.be.entity.Confirm;
import com.ootd.be.entity.ConfirmComment;
import com.ootd.be.entity.ConfirmCommentRepository;
import com.ootd.be.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class ConfirmServiceTest {

    @Autowired
    private ConfirmCommentRepository commentRepository;

    @Test
    public void temp() {

        Confirm confirm = new Confirm();
        confirm.setId(IdGenerator.I.next());

        List<ConfirmComment> temp = commentRepository.findAllByConfirm(confirm);
        for (ConfirmComment c : temp) {
            log.info("{}", c);
        }

    }



}