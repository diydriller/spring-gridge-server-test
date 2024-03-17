package com.gridge.server.common.exception;

import com.gridge.server.common.response.BaseResponseState;
import lombok.Getter;

@Getter
public class AuthenticationException extends BaseException {
    public AuthenticationException(BaseResponseState state) {
        super(state);
    }
}
