package com.gridge.server.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseState {
    SUCCESS("요청 성공", "요청이 성공했습니다"),
    NICKNAME_DUPLICATED("닉네임 중복", "닉네임이 중복되었습니다");
    private final String message;
    private final String code;
}
