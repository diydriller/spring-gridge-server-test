package com.gridge.server.controller.member.dto;

import com.gridge.server.service.member.dto.MemberInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    @Pattern(regexp = "^[a-z0-9_.]*$", message = "아이디는 소문자, 숫자, 밑줄, 마침표만 사용할 수 있습니다")
    @Size(max = 20, message = "아이디는 20자 이하로 입력해주세요")
    @NotBlank(message = "아이디를 입력해주세요")
    private String nickname;
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

     public MemberInfo toInfo() {
        return MemberInfo.builder()
                .nickname(nickname)
                .password(password)
                .build();
    }
}
