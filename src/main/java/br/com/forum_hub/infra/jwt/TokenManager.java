package br.com.forum_hub.infra.jwt;

import br.com.forum_hub.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenManager {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-issuer}")
    private String issuer;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(jwtSecret);
            var instantDate = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("name", user.getFullName())
                    //.withClaim("role", user.getRole().getName().toString())
                    //.withClaim("authorities", user.getRole().getPermissions().stream().map(Permission::getName).toList())
                    .withExpiresAt(instantDate)
                    .sign(algorithm);
        } catch(JWTCreationException e) {
            throw new RuntimeException("JWT generation fail.", e);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT invalid or expired!");
        }
    }
}