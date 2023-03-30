package com.ootd.be.util;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.ootd.be.entity.OotdSeq;
import com.ootd.be.entity.OotdSeqRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

public class IdGenerator {

    private static final Long RANGE = 10000L;
    private static final Long idSeq = 1L;

    private static AtomicLong prefix = new AtomicLong();
    private static AtomicLong postfix = new AtomicLong();

    @Resource
    private OotdSeqRepository ootdSeqRepository;

    public static final IdGenerator I = new IdGenerator();

    private IdGenerator() {
    }

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public Long next() {
        if (prefix == null) {
            OotdSeq max = ootdSeqRepository.findById(idSeq).orElse(newSeq());
            prefix.set(max.getSeq());
            max.setSeq(prefix.incrementAndGet());
        }

        long val = postfix.incrementAndGet();
        if (val >= RANGE) {
            postfix.set(0L);
        }

        return prefix.get() * RANGE + postfix.get();
    }

    private OotdSeq newSeq() {
        OotdSeq seq = new OotdSeq();
        seq.setId(idSeq);
        seq.setSeq(0L);
        seq.setUpdatedAt(LocalDateTime.now());
        ootdSeqRepository.save(seq);
        return seq;
    }

}
