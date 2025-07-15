package com.grepp.teamnotfound.app.model.auth.code;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;


    public boolean isAdmin() {
        return this == ROLE_ADMIN;
    }

    public boolean isUser() {
        return this == ROLE_USER;
    }
}
