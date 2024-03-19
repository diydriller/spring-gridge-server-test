package com.gridge.server.common.exception;

import com.gridge.server.common.response.BaseResponseState;
import lombok.Getter;

@Getter
public class AuthorizationException extends BaseException {
    public AuthorizationException(BaseResponseState state) {
        super(state);
    }
}
