package com.gridge.server.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseState {
    SUCCESS("요청 성공", "요청이 성공했습니다"),
    NICKNAME_DUPLICATED("닉네임 중복", "닉네임이 중복되었습니다"),
    AUTHENTICATION_ERROR("인증 오류", "인증에 실패했습니다"),
    MEMBER_NOT_FOUND("사용자 없음", "사용자를 찾을 수 없습니다"),
    PASSWORD_NOT_MATCH("비밀번호 불일치", "비밀번호가 일치하지 않습니다");
    private final String message;
    private final String code;
}
