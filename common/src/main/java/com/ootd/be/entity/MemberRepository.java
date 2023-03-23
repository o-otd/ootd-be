package com.ootd.be.entity;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class MemberRepository {

    private final Map<String, Member> members = Maps.newHashMap();

    public Optional<Member> findByEmail(String email) {
        return Optional.of(members.get(email));
    }

    public void save(Member member) {
        members.put(member.getEmail(), member);
    }
}
