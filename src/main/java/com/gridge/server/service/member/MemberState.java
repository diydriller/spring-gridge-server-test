package com.gridge.server.service.member;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberState {
    ACTIVATED("활성화"), SUSPENDED("중지"), CANCELLED("탈퇴");
    private final String state;
}
