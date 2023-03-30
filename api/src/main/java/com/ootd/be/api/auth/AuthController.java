package com.ootd.be.api.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ootd.be.config.security.jwt.JwtToken;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @Data
    public static class LoginReqDto {
        private String email;
        private String password;
    }

    @Data
    public static class JoinReqDto {
        private String email;
        private String name;

        private String password;
    }

    @PostMapping("join")
    public ApiResponse join(@RequestBody JoinReqDto req) {
        authService.join(req);
        return ApiResponse.ok();
    }

    @PostMapping("login")
    public ApiResponse login(@RequestBody LoginReqDto req) {
        JwtToken accessToken = authService.login(req);
        return ApiResponse.ok(accessToken);
    }

    @PostMapping("refresh")
    public ApiResponse refresh(@RequestHeader("Authorization") String authorization) {
        log.debug("auth : {}", authorization);
        JwtToken accessToken = authService.refresh();
        return ApiResponse.ok(accessToken);
    }

    @GetMapping("temp")
    public String temp() {
        return "hh";
    }
}
