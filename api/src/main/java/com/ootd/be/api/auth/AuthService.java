package com.ootd.be.api.auth;

import java.util.Date;

import com.ootd.be.config.security.exception.OotdAuthenticationException;
import com.ootd.be.util.IdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ootd.be.api.auth.AuthController.JoinReqDto;
import com.ootd.be.api.auth.AuthController.LoginReqDto;
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

    @Transactional
    public void join(JoinReqDto dto) {

        memberRepository.findByEmail(dto.getEmail()).ifPresent((member) -> {
            throw new ValidationException("등록된 이메일");
        });

        Member member = new Member();
        member.setId(IdGenerator.I.next());
        member.setEmail(dto.getEmail());
        member.setName(dto.getName());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setAuthority(Authority.ROLE_USER);

        memberRepository.save(member);
    }

    public JwtToken login(LoginReqDto req) {

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        JwtToken accessToken = tokenProvider.createAccessToken(authenticate);

        JwtToken refreshToken = tokenProvider.createRefreshToken(authenticate.getName());

        updateRefreshToken(authenticate, refreshToken);

        return accessToken;

    }

    private void updateRefreshToken(Authentication authentication, JwtToken refreshToken) {
        tokenRepository.update(authentication.getName(), refreshToken.getToken(), refreshToken.getExpiration());

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(refreshToken.getExpiration().intValue());

        SpringWebUtil.getHttpServletResponse().addCookie(cookie);
    }

    public JwtToken refresh() {

        Cookie cookie = SpringWebUtil.getCookieByName("refreshToken");
        if (cookie == null) {
            throw new OotdAuthenticationException("리프레시 토큰 오류");
        }
        Claims refreshToken = tokenProvider.parseClaims(cookie.getValue());
        if (refreshToken.getExpiration().before(new Date())) {
            throw new OotdAuthenticationException("리프레시 토큰 오류");
        }

        if (!tokenRepository.validate(refreshToken.getSubject(), cookie.getValue())) {
            throw new OotdAuthenticationException("리프레시 토큰 오류");
        }

        String accessTokenStr = tokenProvider.resolveToken();
        if (accessTokenStr == null) {
            throw new OotdAuthenticationException("인증 토큰 오류");
        }

        Authentication authentication = tokenProvider.getAuthentication(accessTokenStr);
        JwtToken renewedAccessToken = tokenProvider.createAccessToken(authentication);

        JwtToken renewedRefreshToken = tokenProvider.createRefreshToken(authentication.getName());

        updateRefreshToken(authentication, renewedRefreshToken);

        return renewedAccessToken;
    }

}
