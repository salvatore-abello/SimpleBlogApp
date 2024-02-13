package it.salvatoreabello.simpleblogapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.salvatoreabello.simpleblogapp.model.UserModel;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JWTUtil {
    private final SecretKey key;
    private final Integer expirationHours;

    public JWTUtil(String secretKey, Integer exphours) {
        // If the secret is too small we need to derive it (hmacShaKeyFor wants a minimum of 256 bits)
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(hash);
        this.expirationHours = exphours;
    }

    public String createJWT(UserModel entity){
        Instant now = Instant.now();
        Instant expirationTime = now.plus(this.expirationHours, ChronoUnit.HOURS);
        return Jwts.builder()
                .subject(entity.getEmail())
                .expiration(Date.from(expirationTime))
                .issuedAt(Date.from(now))
                .signWith(key)
                .compact();
    }

    public String getJWT(String jwtString){
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtString);

        return jws.getPayload().getSubject();
    }
}
