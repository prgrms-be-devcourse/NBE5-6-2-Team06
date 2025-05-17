package com.grepp.matnam.app.controller.api.user.payload;

import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.user.entity.User;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
public class UserSignupRequest {
    @NotBlank(message = "아이디는 필수입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 19, message = "나이는 19세 이상이어야 합니다.")
    private Integer age;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    public User toEntity() {
        User user = new User();
        user.setUserId(this.userId);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setAddress(this.address);
        user.setNickname(this.nickname);
        user.setAge(this.age);
        user.setGender(this.gender);
        return user;
    }
}