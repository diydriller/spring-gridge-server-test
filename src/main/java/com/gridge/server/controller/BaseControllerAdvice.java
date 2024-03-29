package com.gridge.server.controller;

import com.gridge.server.common.exception.AuthenticationException;
import com.gridge.server.common.exception.AuthorizationException;
import com.gridge.server.common.exception.BaseException;
import com.gridge.server.common.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.gridge.server.common.response.BaseResponseState.MISSING_PARAMETER;

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
                .message("요청 오류")
                .code(String.join("/",errorList))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public BaseResponse<?> handleMissingParameterException(){
        return new BaseResponse<>(MISSING_PARAMETER);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public BaseResponse<?> handleAuthenticationException(AuthenticationException e){
        return new BaseResponse<>(e.getState());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public BaseResponse<?> handleAuthorizationException(AuthorizationException e){
        return new BaseResponse<>(e.getState());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public BaseResponse<?> handleBaseException(BaseException e){
        return new BaseResponse<>(e.getState());
    }
}
