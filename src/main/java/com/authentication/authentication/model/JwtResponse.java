package com.authentication.authentication.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {
    private final String token;
    private final String userId;
}
