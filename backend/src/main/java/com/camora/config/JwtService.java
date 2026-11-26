package com.camora.config;

import com.camora.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(
            @Value("${camora.jwt.secret}") String secret,
            @Value("${camora.jwt.expiration-ms}") long expirationMs) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    public String gerarToken(UserPrincipal principal) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMs);
        return Jwts.builder()
                .subject(principal.getEmail())
                .claims(Map.of(
                        "uid", principal.getId(),
                        "role", principal.getRole().name(),
                        "nome", principal.getNome()
                ))
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(secretKey)
                .compact();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public Role extrairRole(String token) {
        String roleStr = extrairClaims(token).get("role", String.class);
        return Role.valueOf(roleStr);
    }

    public boolean tokenValido(String token) {
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
}
