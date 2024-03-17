package com.gridge.server.controller.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateKakaoMemberRequest {
    private String accessToken;
}
