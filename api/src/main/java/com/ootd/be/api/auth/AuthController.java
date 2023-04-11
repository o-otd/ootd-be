package com.ootd.be.api.auth;

import com.ootd.be.api.ApiResponse;
import com.ootd.be.config.security.jwt.JwtToken;
import com.ootd.be.config.swagger.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse join(JoinReqDto req) {
        authService.join(req);
        return ApiResponse.ok();
    }

    @PostMapping("login")
    public ApiResponse login(LoginReqDto req) {
        JwtToken accessToken = authService.login(req);
        return ApiResponse.ok(accessToken);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.SCHEME)})
    @PostMapping("refresh")
    public ApiResponse refresh() {
        JwtToken accessToken = authService.refresh();
        return ApiResponse.ok(accessToken);
    }

    @GetMapping("temp")
    public String temp() {
        return "hh";
    }
}
