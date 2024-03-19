package com.gridge.server.service.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.gridge.server.common.util.StringUtil.TOKEN_CLAIM_FIELD;

@Component
public class TokenService {
    @Value("${security.secret}")
    private String SECRET;

    public DecodedJWT verifyToken(String jwtToken) {
        return JWT.require(Algorithm.HMAC512(SECRET)).build().verify(jwtToken);
    }

    public String createToken(Long userId) {
        return JWT.create()
                .withClaim(TOKEN_CLAIM_FIELD, userId)
                .sign(Algorithm.HMAC512(SECRET));
    }
}
