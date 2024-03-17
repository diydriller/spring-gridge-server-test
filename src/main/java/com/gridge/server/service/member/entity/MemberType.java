package com.gridge.server.service.member.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberType {
    LOCAL("기본회원"), KAKAO("카카오회원");
    private final String type;
}
