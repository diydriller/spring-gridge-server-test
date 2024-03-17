package com.gridge.server.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.gridge.server.common.response.BaseResponseState.SUCCESS;

@Getter
@Builder
@AllArgsConstructor
public class BaseResponse<T> {
    private T data;
    private String message;
    private String code;

    public BaseResponse(T data) {
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.data = data;
    }

    public BaseResponse(BaseResponseState state) {
        this.message = state.getMessage();
        this.code = state.getCode();
    }
}
