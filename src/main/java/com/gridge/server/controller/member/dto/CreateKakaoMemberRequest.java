package com.gridge.server.controller.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateKakaoMemberRequest {
    @NotBlank(message = "토큰를 입력해주세요")
    private String accessToken;
}
