package com.gridge.server.service.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MemberInfo {
    private Long id;
    private String nickname;
    private String password;
    private String phoneNumber;
    private String name;
    private String birthday;
    private String imageUrl;
}
