package com.gridge.server.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gridge.server.common.exception.AuthenticationException;
import com.gridge.server.dataManager.member.MemberRepository;
import com.gridge.server.service.common.TokenService;
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
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(ACCESS_TOKEN);
        if(token == null){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }
        token = token.replace(TOKEN_PREFIX, "");
        if(token.isEmpty()){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }

        Long userId;
        try{
            DecodedJWT jwt = tokenService.verifyToken(token);
            userId = jwt.getClaim(TOKEN_CLAIM_FIELD).asLong();
        }
        catch (Exception e){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }

        var member = memberRepository.findById(userId).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        request.setAttribute("member", member);

        return true;
    }
}
