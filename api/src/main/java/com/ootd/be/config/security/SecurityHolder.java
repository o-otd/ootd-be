package com.ootd.be.config.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ootd.be.entity.Member;

public class SecurityHolder {

    private static final String ANONYMOUS = "ANONYMOUS";

    public static Member get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return new Member() {{
                setEmail(ANONYMOUS);
            }};
        } else {
            return new Member() {{
                setEmail(authentication.getName());
                setName((String) authentication.getDetails());
            }};
        }
    }

}
