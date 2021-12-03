package com.authentication.authentication.model;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

    private String userId;

    public CustomUserDetails(String userName, String userId, String password, List<GrantedAuthority> authorities) {
        super(userName, password, authorities);
        this.userId = userId;
    }
}
