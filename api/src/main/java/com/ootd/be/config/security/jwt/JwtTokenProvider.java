package com.ootd.be.config.security.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ootd.be.config.security.CustomGrantedAuthority;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.web.SpringWebUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final Long access_token_expire_time;
    private final Long refresh_token_expire_time;

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.access-token.expire-time:600}") Long access_token_expire_time
            , @Value("${jwt.refresh-token.expire-time:3600}") Long refresh_token_expire_time
            , @Value("${jwt.secret-key:ootd1!2@3#}") String secretKey) {
        this.access_token_expire_time = access_token_expire_time;
        this.refresh_token_expire_time = refresh_token_expire_time;
        this.key = Keys.hmacShaKeyFor(Sha512DigestUtils.sha(secretKey));
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String auths = (String) claims.getOrDefault(AUTHORITIES_KEY, "");
        if (!StringUtils.hasText(auths)) {
            throw new IllegalStateException("No Authority Info.");
        }

        List<GrantedAuthority> authorities = Arrays.stream(auths.split(","))
                                                   .map(CustomGrantedAuthority::of)
                                                   .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }

    public JwtToken createAccessToken(Authentication authentication) {

        Claims claims = Jwts.claims().setSubject(authentication.getName());

        String auths = authentication.getAuthorities().stream()
                                     .map(GrantedAuthority::getAuthority)
                                     .collect(Collectors.joining(","));
        claims.put(AUTHORITIES_KEY, auths);

        long now = System.currentTimeMillis();

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setIssuedAt(new Date(now))
                           .setExpiration(new Date(now + access_token_expire_time * 1000L))
                           .signWith(key, SignatureAlgorithm.HS512)
                           .compact();

        return JwtToken.of(access_token_expire_time, token);
    }

    public JwtToken createRefreshToken(String username) {

        Claims claims = Jwts.claims().setSubject(username);

        long now = System.currentTimeMillis();

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setIssuedAt(new Date(now))
                           .setExpiration(new Date(now + refresh_token_expire_time * 1000L))
                           .signWith(key, SignatureAlgorithm.HS512)
                           .compact();

        return JwtToken.of(refresh_token_expire_time, token);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            if (claims.getExpiration().before(new Date())) {
                throw new ValidationException("expired access token");
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (ExpiredJwtException e) { // 만료된 토큰이 더라도 일단 파싱을 함
            return e.getClaims();
        }
    }

    public String resolveToken() {
        return resolveToken(SpringWebUtil.getHttpServletRequest());
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken)) {
            return null;
        }

        if (!bearerToken.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        return bearerToken.substring("Bearer ".length());

    }

}
