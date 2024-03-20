package com.gridge.server.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseState {
    SUCCESS("요청 성공", "요청이 성공했습니다"),
    NICKNAME_DUPLICATED("닉네임 중복", "닉네임이 중복되었습니다"),
    AUTHENTICATION_ERROR("인증 오류", "인증에 실패했습니다"),
    AUTHORIZATION_ERROR("권한 오류", "권한이 없습니다"),
    MEMBER_NOT_FOUND("사용자 없음", "사용자를 찾을 수 없습니다"),
    PASSWORD_NOT_MATCH("비밀번호 불일치", "비밀번호가 일치하지 않습니다"),
    NOT_KAKAO_MEMBER("카카오 회원 아님", "카카오 회원이 아닙니다"),
    NOT_IMAGE_FILE("파일 업로드 오류", "이미지 파일이 아닙니다"),
    MISSING_PARAMETER("요청 오류", "필수 파라미터가 누락되었습니다"),
    POST_NOT_FOUND("게시글 없음", "게시글을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND("댓글 없음", "댓글을 찾을 수 없습니다"),
    REPORT_NOT_FOUND("신고 없음", "신고를 찾을 수 없습니다");
    private final String message;
    private final String code;
}
