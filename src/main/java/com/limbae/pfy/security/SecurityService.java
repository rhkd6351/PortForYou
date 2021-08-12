package com.limbae.pfy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecurityService {
    private static final String SECRET_KEY = "limbaeApplicationSecretCodeChurchWithJeongam";

    //로그인시 같이 토큰 던지기
    public String createToken(Map<String, String> data){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //알고리즘
        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8); //시크릿코드 바이트배열 변환
        Long expTime = 1000L * 60L * 60L * 2L;

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg","HS256");

        Map<String, Object> payload = new HashMap<>();
        payload.put("role", data.get("role"));
        payload.put("email", data.get("email"));


        return Jwts.builder()
                .setSubject("user")
                .setHeader(header)
                .setClaims(payload)
                .signWith(signatureAlgorithm, secretKeyBytes)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .compact();
    }

    //토큰 검증시
    public Map<String, Object> getSubject(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Map<String, Object> data = claims;
        return data;
    }
}
