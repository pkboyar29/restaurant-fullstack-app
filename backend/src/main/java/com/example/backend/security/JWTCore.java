package com.example.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTCore {
    @Autowired
    public JWTCore() {}

    @Value("${token.secretKey}")
    private String secretKey;

    @Value("${token.expire}")
    private int expire;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("username", userDetails.getUsername());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setExpiration(new Date((new Date()).getTime() + expire))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getSubjectFromJwt(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody().getSubject();
    }
}
