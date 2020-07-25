package drii.pingeso.backend.Security.security.RestorePassword;

import drii.pingeso.backend.SQL.models.User;
import drii.pingeso.backend.Security.security.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Service
@Data
public class ConfirmationTokenProvider {

    @Autowired
    private JwtProperties jwtProperties;

    private String secretKeyForEmail;

    @PostConstruct
    protected void init() {
        secretKeyForEmail = Base64.getEncoder().encodeToString(jwtProperties.getSecretKeyForEmail().getBytes());
    }

    public String createToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMsForRestorePassword());

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKeyForEmail)//
                .compact();
    }
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKeyForEmail).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateConfirmationToken(String token) {
        try {
            Date date = Jwts.parser().setSigningKey(secretKeyForEmail).parseClaimsJws(token).getBody().getExpiration();
            if (date.before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}