package com.ootd.be.config.security.jwt;

import com.ootd.be.config.security.exception.OotdAuthenticationException;
import com.ootd.be.entity.Member;
import com.ootd.be.entity.MemberRepository;
import com.ootd.be.entity.RefreshToken;
import com.ootd.be.entity.RefreshTokenRepository;
import com.ootd.be.util.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class TokenRepository {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository tokenRepository;

    public boolean validate(String username, String refreshToken) {
        Optional<RefreshToken> token = findActiveToken(username);
        return refreshToken.equals(token.orElseGet(RefreshToken::new).getRefreshToken());
    }

    @Transactional
    public void update(String username, String refreshToken, Long expiration) {
        Optional<RefreshToken> findToken = findActiveToken(username);
        findToken.ifPresent(token -> {
            token.setExpired(true);
            tokenRepository.save(token);
        });

        // todo. 이거 맞아..?
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new OotdAuthenticationException("존재하지 않는 계정"));

        RefreshToken newToken = new RefreshToken();
        newToken.setId(IdGenerator.I.next());
        newToken.setMember(member);
        newToken.setExpired(false);
        newToken.setRefreshToken(refreshToken);
        newToken.setExpiredAt(LocalDateTime.now().plusSeconds(expiration));

        tokenRepository.save(newToken);
    }

    public void remove(String username) {
        Optional<RefreshToken> findToken = findActiveToken(username);
        findToken.ifPresent(token -> {
            token.setExpired(true);
            tokenRepository.save(token);
        });
    }

    private Optional<RefreshToken> findActiveToken(String username) {
        return tokenRepository.findByMember_EmailAndExpiredIsFalse(username);
    }
}
