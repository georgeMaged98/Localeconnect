package com.localeconnect.app.authentication.service;

import com.localeconnect.app.authentication.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
@Service
@Component
public class JwtUtil {
    private final static Long expiration = SecurityConstants.JWT_EXPIRATION;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public String generateToken(String username, String email) {
        Map<String, String> claims = Map.of("username", username, "email", email);
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get("id"))
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
        System.out.println("New token :");
        System.out.println(token);
        return token;
    }

    /* public boolean validateToken(String token) {
        try {
            if (isExpired(token)) {
                throw new AuthenticationCredentialsNotFoundException("JWT is expired.");
            }

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", ex.fillInStackTrace());
        }
    } */

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();
    }

    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
