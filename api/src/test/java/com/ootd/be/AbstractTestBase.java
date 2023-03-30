package com.ootd.be;

import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ootd.be.config.security.CustomGrantedAuthority;
import com.ootd.be.entity.Authority;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTestBase {

    @PostConstruct
    public void init() {
        SecurityContextHolder.getContext().setAuthentication(makeToken("aa@aa.com", "testerA", Authority.ROLE_USER));
    }

    public UsernamePasswordAuthenticationToken makeToken(String email, String name, Authority authority) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, "", Set.of(new CustomGrantedAuthority(authority)));
        token.setDetails(name);
        return token;
    }

}
