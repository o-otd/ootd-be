package com.ootd.be.config.security.jwt;

import lombok.Getter;

public class JwtToken {

    @Getter
    private Long expiration;
    @Getter
    private String token;

    public static JwtToken of(Long expiration, String token) {
        JwtToken vo = new JwtToken();
        vo.expiration = expiration;
        vo.token = token;
        return vo;
    }

}
