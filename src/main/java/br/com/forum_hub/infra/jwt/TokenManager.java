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

    private final long ONE_HOUR_IN_MINUTES = 60;

    private final long ONE_DAY_IN_HOURS = 24;

    public String generateAccessToken(User user) {
        return generateToken(user, 2 * ONE_HOUR_IN_MINUTES);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, ONE_DAY_IN_HOURS * ONE_HOUR_IN_MINUTES);
    }

    private String generateToken(User user, long timeInMinutes) {
        try {
            var algorithm = Algorithm.HMAC256(jwtSecret);
            var instantDate = LocalDateTime.now().plusMinutes(timeInMinutes).toInstant(ZoneOffset.of("-03:00"));
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