package com.ootd.be.entity;

public enum Authority {

    ROLE_ADMIN("ADMIN"), ROLE_USER("USER")
    ;

    public final String role;

    Authority(String role) {
        this.role = role;
    }

    public static Authority from(String role) {
        try {
            return Authority.from(("ROLE_" + role));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
