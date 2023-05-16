package com.example.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private final Algorithm algorithm;

    public JwtTokenUtil(Environment env) {
        var jwtSecret = env.getRequiredProperty("jwt.secret");
        var secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        var base64SecretBytes = Base64.getEncoder().encode(secretBytes);
        algorithm = Algorithm.HMAC512(base64SecretBytes);
    }

    public String generateLoginToken(String subject, Map<String, Object> extraClaims) {
        return generateToken(subject, extraClaims);
    }

    private String generateToken(String subject, Map<String, Object> extraClaims) {
        var issuedAt = new Date();
        var expiresAt = new Date(System.currentTimeMillis() + 432_000_000);
        var claims = new HashMap<String, Object>();
        claims.put("extraClaims", extraClaims);
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withPayload(claims)
                .sign(algorithm);
    }

    public boolean isTokenUnexpired(String token) {
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        var expiresAt = getExpiresAt(token);
        return expiresAt != null && expiresAt.before(new Date());
    }

    public String getSubjectFromToken(String token) {
        return getClaim(token, DecodedJWT::getSubject);
    }

    public Long getExpireDays(String token) {
        var expiresAt = getExpiresAt(token);
        if (expiresAt == null) {
            return null;
        }
        long diffMillis = new Date().getTime() - expiresAt.getTime();
        return TimeUnit.MILLISECONDS.toDays(diffMillis);
    }

    public Date getExpiresAt(String token) {
        return getClaim(token, DecodedJWT::getExpiresAt);
    }

    public Map<String, Object> getExtraClaims(String token) {
        var claim = getClaim(token, DecodedJWT::getClaims).get("extraClaims");
        if (claim == null || claim.isNull()) {
            return Map.of();
        }
        return claim.asMap();
    }

    private <T> T getClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        var decodedJWT = JWT.decode(token);
        algorithm.verify(decodedJWT);
        return claimsResolver.apply(decodedJWT);
    }
}
