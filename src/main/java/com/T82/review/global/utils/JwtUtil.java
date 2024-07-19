package com.T82.review.global.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secret;

    //JWT 토큰 정보 보기 작업
    public TokenInfo parseToken(String token) {
    Claims payload = (Claims) Jwts.parser()
                .verifyWith(secret)
                .build()
                .parse(token)
                .getPayload();
        return TokenInfo.fromClaims(payload);
    }
    public JwtUtil(
            @Value("${jwt.secret}") String secret
    ) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
    }
}
