package com.ootd.be.api;

import com.ootd.be.api.auth.AuthController;
import com.ootd.be.api.auth.AuthService;
import com.ootd.be.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class ServiceTestBase {

    private static final String email = "aa@aa.com";
    private static final String password = "123";
    private static final String name = "aaa";

    @BeforeAll
    public static void createAuthenticate() {

        log.info("계정 생성 및 로그인 처리");

        AuthController.JoinReqDto req = new AuthController.JoinReqDto();
        req.setEmail(email);
        req.setPassword(password);
        req.setName(name);

        AuthService authService = SpringUtil.getBean(AuthService.class);

        authService.join(req);

    }

    @BeforeEach
    public void login() {

        AuthenticationManagerBuilder authenticationManagerBuilder = SpringUtil.getBean(AuthenticationManagerBuilder.class);

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

    }

}