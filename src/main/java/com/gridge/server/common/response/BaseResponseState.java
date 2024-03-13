package com.gridge.server.common.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BaseResponseState {
    ;
    private final String message;
    private final String code;
}
