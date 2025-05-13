package com.grepp.matnam.app.controller.api.user.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeactivateRequest {

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}