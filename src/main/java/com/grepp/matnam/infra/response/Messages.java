package com.grepp.matnam.infra.response;

public class Messages {

    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    public static final String USER_EMAIL_DUPLICATE = "이미 존재하는 이메일입니다.";
    public static final String USER_ID_DUPLICATE = "이미 존재하는 아이디입니다.";
    public static final String USER_PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다.";
    public static final String USER_CURRENT_PASSWORD_MISMATCH = "현재 비밀번호가 일치하지 않습니다.";
    public static final String USER_SUSPENDED = "정지된 계정입니다.";
    public static final String USER_INACTIVE = "비활성화(탈퇴) 계정입니다.";
    public static final String PASSWORD_INVALID_FORMAT = "비밀번호는 8~20자의 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.";
    public static final String SERVER_ERROR = "서버 오류가 발생했습니다.";
    public static final String SUCCESS_SIGNUP = "회원가입 성공";
    public static final String SUCCESS_SIGNIN = "로그인 성공";
    public static final String SUCCESS_PREFERENCE = "취향 설정 성공";
    public static final String SUCCESS_PREFERENCE_UPDATE = "취향 변경 성공";
    public static final String SUCCESS_DEACTIVATE = "회원 탈퇴 성공";
    public static final String SUCCESS_PASSWORD_CHANGE = "비밀번호 변경 성공";
    public static final String SUCCESS_TEMPERATURE = "매너온도 조회 성공";
}