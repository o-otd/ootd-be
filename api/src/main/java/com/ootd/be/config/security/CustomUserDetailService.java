package com.ootd.be.config.security;

import com.ootd.be.config.security.exception.OotdAuthenticationException;
import com.ootd.be.util.StringUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ootd.be.entity.Member;
import com.ootd.be.entity.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username).map(this::createUserDetails).orElseThrow(() -> new OotdAuthenticationException("사용자 정보 없음"));
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                   .username(member.getEmail())
                   .password(member.getPassword())
                   .roles(member.getAuthority().role)
                   .build();
    }

}
