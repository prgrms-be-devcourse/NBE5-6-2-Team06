package com.grepp.matnam.app.controller.api.user.payload;

import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.auth.code.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String email;
    private String address;
    private String nickname;
    private Integer age;
    private Gender gender;
    private Float temperature;
    private Role role;
}