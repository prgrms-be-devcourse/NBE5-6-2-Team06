package com.grepp.matnam.infra.auth;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import static com.grepp.matnam.infra.response.Messages.PASSWORD_INVALID_FORMAT;

@Slf4j
public class PasswordValidator {

    // 비밀번호 정책: 최소 8자, 최대 20자, 하나 이상의 숫자, 하나 이상의 대문자, 하나 이상의 소문자, 하나 이상의 특수문자
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(String password) {
        if (password == null) {
            return false;
        }

        boolean isValid = pattern.matcher(password).matches();

        if (!isValid) {
            log.debug("유효하지 않은 비밀번호 형식");
        }

        return isValid;
    }

    public static void validate(String password) {
        if (!isValid(password)) {
            throw new IllegalArgumentException(PASSWORD_INVALID_FORMAT);
        }
    }
}