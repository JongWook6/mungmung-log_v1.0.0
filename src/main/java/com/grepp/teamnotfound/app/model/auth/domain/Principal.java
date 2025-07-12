package com.grepp.teamnotfound.app.model.auth.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

// 인증용 Spring.User 상속
public class Principal extends User {

    private final Long userId;

    public Principal(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

}
