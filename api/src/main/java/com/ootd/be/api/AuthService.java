package com.ootd.be.api;

import java.util.Date;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ootd.be.api.AuthController.JoinReqDto;
import com.ootd.be.api.AuthController.LoginReqDto;
import com.ootd.be.config.security.jwt.JwtToken;
import com.ootd.be.config.security.jwt.JwtTokenProvider;
import com.ootd.be.config.security.jwt.TokenRepository;
import com.ootd.be.entity.Authority;
import com.ootd.be.entity.Member;
import com.ootd.be.entity.MemberRepository;
import com.ootd.be.exception.ValidationException;
import com.ootd.be.util.web.SpringWebUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenRepository tokenRepository;

    public void join(JoinReqDto dto) {
        Member member = new Member() {{
            setEmail(dto.getEmail());
            setName(dto.getName());
            setPassword(passwordEncoder.encode(dto.getPassword()));
            setAuthorities(Set.of(Authority.ROLE_USER));
        }};

        memberRepository.save(member);
    }

    public JwtToken login(LoginReqDto req) {

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        JwtToken accessToken = tokenProvider.createAccessToken(authenticate);

        JwtToken refreshToken = tokenProvider.createRefreshToken(authenticate.getName());
        tokenRepository.update(authenticate.getName(), refreshToken.getToken());
        setRefreshToken(refreshToken);

        return accessToken;

    }

    private void setRefreshToken(JwtToken refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(refreshToken.getExpiration().intValue());

        SpringWebUtil.getHttpServletResponse().addCookie(cookie);
    }

    public JwtToken refresh() {

        Cookie cookie = SpringWebUtil.getCookieByName("refreshToken");
        Claims refreshToken = tokenProvider.parseClaims(cookie.getValue());
        if (refreshToken.getExpiration().before(new Date())) {
            throw new ValidationException("expired refresh token");
        }

        if (!tokenRepository.validate(refreshToken.getSubject(), cookie.getValue())) {
            throw new ValidationException("invalid refresh token");
        }

        String accessTokenStr = tokenProvider.resolveToken();
        if (accessTokenStr == null) {
            throw new ValidationException("session timeout");
        }

        Authentication authentication = tokenProvider.getAuthentication(accessTokenStr);
        JwtToken renewedAccessToken = tokenProvider.createAccessToken(authentication);

        JwtToken renewedRefreshToken = tokenProvider.createRefreshToken(authentication.getName());

        setRefreshToken(renewedRefreshToken);

        return renewedAccessToken;
    }

}
