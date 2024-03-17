package com.gridge.server.controller.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckNicknameRequest {
    @Pattern(regexp = "^[a-z0-9_.]*$", message = "아이디는 소문자, 숫자, 밑줄, 마침표만 사용할 수 있습니다")
    @Size(max = 20, message = "아이디는 20자 이하로 입력해주세요")
    @NotBlank(message = "아이디를 입력해주세요")
    private String nickname;
}
