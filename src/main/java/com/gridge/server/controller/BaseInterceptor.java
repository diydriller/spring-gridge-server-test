package com.gridge.server.controller;

import com.gridge.server.common.exception.AuthenticationException;
import com.gridge.server.service.common.TokenService;
import com.gridge.server.service.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.gridge.server.common.response.BaseResponseState.AUTHENTICATION_ERROR;
import static com.gridge.server.common.util.StringUtil.*;

@Component
@RequiredArgsConstructor
public class BaseInterceptor implements HandlerInterceptor {
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(ACCESS_TOKEN).replace(TOKEN_PREFIX, "");
        if(token.isEmpty()){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }

        try{
            tokenService.verifyToken(token);
        }
        catch (Exception e){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }

        Member member = (Member) request.getSession().getAttribute(SESSION_KEY);
        if(member == null){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }

        return true;
    }
}
