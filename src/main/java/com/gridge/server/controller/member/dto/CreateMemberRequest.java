package com.gridge.server.controller.member.dto;

import com.gridge.server.service.member.dto.MemberInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {
    @Pattern(regexp = "^[a-z0-9_.]*$", message = "아이디는 소문자, 숫자, 밑줄, 마침표만 사용할 수 있습니다")
    @Size(max = 20, message = "아이디는 20자 이하로 입력해주세요")
    @NotBlank(message = "아이디를 입력해주세요")
    private String nickname;
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    @Pattern(regexp = "^\\+\\d{1,3} \\d{2}-\\d{4}-\\d{4}$", message = "전화번호 형식에 맞게 입력해주세요")
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phoneNumber;
    @Size(max = 20, message = "아이디는 20자 이하로 입력해주세요")
    @NotBlank(message = "이름를 입력해주세요")
    private String name;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생일 형식에 맞게 입력해주세요")
    @NotBlank(message = "생일을 입력해주세요")
    private String birthday;

    public MemberInfo toInfo() {
        return MemberInfo.builder()
                .nickname(nickname)
                .password(password)
                .phoneNumber(phoneNumber)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
