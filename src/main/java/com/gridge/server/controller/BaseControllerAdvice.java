package com.gridge.server.controller;

import com.gridge.server.common.exception.AuthenticationException;
import com.gridge.server.common.exception.BaseException;
import com.gridge.server.common.response.BaseResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class BaseControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BaseResponse<?> handleValidationException(BindException e){
        BindingResult result = e.getBindingResult();
        List<String> errorList = new ArrayList<>();
        result.getFieldErrors()
                .forEach((fieldError) ->
                errorList.add(fieldError.getDefaultMessage())
        );
        return BaseResponse.builder()
                .message("입력값이 올바르지 않습니다")
                .code(String.join("/",errorList))
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public BaseResponse<?> handleAuthenticationException(AuthenticationException e){
        return new BaseResponse<>(e.getState());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public BaseResponse<?> handleBaseException(BaseException e){
        return new BaseResponse<>(e.getState());
    }
}
