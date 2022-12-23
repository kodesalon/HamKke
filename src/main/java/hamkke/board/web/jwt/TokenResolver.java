package hamkke.board.web.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class TokenResolver {

    private static final String ISSUER = "hamkke";
    private final String secret;
    private final Long tokenValidityInMilliseconds;

    public TokenResolver(final @Value("${jwt.secret}") String secret,
                         final @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }


    public String createToken(final Long userId) {
        Date now = new Date();
        generateKey();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("userId", userId)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(tokenValidityInMilliseconds).toMillis()))
                .compact();
    }

    public Long getUserId(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(generateKey()).build().parseClaimsJws(token);
            return true;
        } catch (final SignatureException | SecurityException | MalformedJwtException exception) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        } catch (final ExpiredJwtException exception) {
            throw new JwtException("토큰의 유효기간이 만료되었습니다.");
        } catch (final UnsupportedJwtException exception) {
            throw new JwtException("지원하지 않는 토큰입니다.");
        } catch (final IllegalArgumentException exception) {
            throw new JwtException("JWT 토큰이 잘못되었습니다.");
        }
    }

    private Key generateKey() {
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }
}
