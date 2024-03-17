package com.gridge.server.service.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfo {
    private String nickname;
    private String password;
    private String phoneNumber;
    private String name;
    private String birthday;
}
