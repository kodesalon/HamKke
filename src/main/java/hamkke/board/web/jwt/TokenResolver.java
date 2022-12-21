package hamkke.board.web.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class TokenResolver {

    private static final String ISSUER = "hamkke";
    private static final String SECRET_KEY = "password1234";
    private static final int TOKEN_EXPIRED_TIME = 30;
    private static final byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    private static final Key signatureKey= new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

    public String createToken(final Long userId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(TOKEN_EXPIRED_TIME).toMillis()))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, signatureKey)
                .compact();
    }

    public Long getUserId(final String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (final SignatureException exception) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        } catch (final ExpiredJwtException exception) {
            throw new JwtException("토큰의 유효기간이 만료되었습니다.");
        }
    }
}
