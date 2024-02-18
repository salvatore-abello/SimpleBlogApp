package it.salvatoreabello.simpleblogapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public UserModel getCurrentUser() throws Exception {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            Claims c = this.getJWT(extractJwtToken(request));

            LinkedHashMap<String, Object> map =
                    (LinkedHashMap<String, Object>) c.get("user"); // Maybe add a check?

            return UserModel.builder()
                    .id(Integer.parseInt(c.getSubject()))
                    .name((String)map.get("name"))
                    .surname((String)map.get("surname"))
                    .build();
        }
        throw new Exception("Not called in the context of an HTTP request");
    }

    public String createJWT(UserModel entity){
        Instant now = Instant.now();
        Instant expirationTime = now.plus(this.expirationHours, ChronoUnit.HOURS);
        return Jwts.builder()
                .subject(String.valueOf(entity.getId()))
                .claim("user", entity)
                .expiration(Date.from(expirationTime))
                .issuedAt(Date.from(now))
                .signWith(key)
                .compact();
    }

    public Claims getJWT(String jwtString){
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtString);

        return jws.getPayload();
    }

    public String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.split(" ")[1];
        }
        return null;
    }
}
