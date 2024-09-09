package org.example.ted.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.ted.models.User;
import org.example.ted.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.example.ted.security.SecurityConstants.EXPIRATION_TIME;
import static org.example.ted.security.SecurityConstants.SECRET;

@Component
public class JWTGenerator {
    private final UserRepository userRepository;

    public JWTGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateJWT(Authentication authentication) {
        String email = authentication.getName();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
        User user = userRepository.findByEmail(email).orElse(null);
        long id = user.getId();

        String token = Jwts.builder()
                .setSubject(email)
                .setId(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}
