package com.gridge.server.service.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {
    @Value("${security.secret}")
    private String SECRET;

    public void verifyToken(String jwtToken) {
        JWT.require(Algorithm.HMAC512(SECRET)).build().verify(jwtToken);
    }

    public String createToken(Long userId) {
        return JWT.create()
                .withClaim("id", userId)
                .sign(Algorithm.HMAC512(SECRET));
    }
}
