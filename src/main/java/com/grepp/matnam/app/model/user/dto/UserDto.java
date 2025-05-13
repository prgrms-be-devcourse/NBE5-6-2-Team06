package com.grepp.matnam.app.model.user.dto;

import com.grepp.matnam.app.model.auth.code.Role;
import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.app.model.user.code.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String userId;

    private String password;

    private String email;

    private String address;

    private String nickname;

    private int age;

    private Role role;

    private Gender gender;

    private float temperature;

    private Status status;

    private LocalDate dueDate;

    private Integer suspendDuration;

    private String dueReason;

    private Boolean activated;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
