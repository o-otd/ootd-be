package com.ootd.be.config.security;

import org.springframework.security.core.GrantedAuthority;

import com.ootd.be.entity.Authority;

public class CustomGrantedAuthority implements GrantedAuthority {

    private final Authority authority;

    public CustomGrantedAuthority(Authority authority) {
        this.authority = authority;
    }

    public static CustomGrantedAuthority of(String authority) {
        return new CustomGrantedAuthority(Authority.valueOf(authority));
    }

    @Override
    public String getAuthority() {
        return authority.name();
    }

}
