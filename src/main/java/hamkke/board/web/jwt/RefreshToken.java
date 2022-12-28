package hamkke.board.web.jwt;

import hamkke.board.domain.user.User;
import io.jsonwebtoken.JwtException;

import java.time.LocalDateTime;

public class RefreshToken {

    private Long id;
    private User user;
    private String token;
    private LocalDateTime expirationTime;

    public RefreshToken(final User user, final String token, final LocalDateTime expirationTime) {
        this.user = user;
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public void switchToken(final String newToken) {
        token = newToken;
    }

    public void checkExpirationTime(final LocalDateTime time) {
        if (time.isAfter(expirationTime)) {
            throw new JwtException("Refresh 토큰이 만료되었습니다.");
        }
    }
}
