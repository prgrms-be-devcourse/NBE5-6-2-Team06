package com.grepp.matnam.app.model.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final UserDto userDto;

    public CustomOAuth2User(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userDto.getRole()));
        return authorities;
    }

    @Override
    public String getName() {
        return userDto.getName();
    }

    public String getUserId() {
        return userDto.getUserId();
    }

    public String getEmail() {
        return userDto.getEmail();
    }

    public String getProvider() {
        return userDto.getProvider();
    }

    public String getProviderId() {
        return userDto.getProviderId();
    }
}