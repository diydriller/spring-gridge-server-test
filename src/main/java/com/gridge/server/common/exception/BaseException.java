package com.gridge.server.common.exception;

import com.gridge.server.common.response.BaseResponseState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private BaseResponseState state;
}
