package com.grepp.matnam.app.model.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String role;
    private String name;
    private String username;
    private String userId;
    private String email;
    private String provider;
    private String providerId;
}