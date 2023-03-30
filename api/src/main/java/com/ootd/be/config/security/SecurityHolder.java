package com.ootd.be.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ootd.be.entity.Member;

public class SecurityHolder {

    public static Member get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = new Member() {{
            setEmail(authentication.getName());
            setName((String) authentication.getDetails());
        }};

        return member;
    }

}
