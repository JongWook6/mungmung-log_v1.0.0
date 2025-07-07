package com.grepp.teamnotfound.app.model.auth.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class Principal extends User {
    public Principal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static Principal createPrincipal(com.grepp.teamnotfound.app.model.user.entity.User user,
                                            List<SimpleGrantedAuthority> authorities) {
        return new Principal(user.getEmail(), user.getPassword(), authorities);
    }
}
