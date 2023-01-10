package hamkke.board.web.jwt;

import hamkke.board.domain.user.vo.LoginId;
import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Embedded
    private LoginId loginId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    public RefreshToken(final LoginId loginId, final String token, final LocalDateTime expirationTime) {
        this.loginId = loginId;
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public void switchToken(final String newToken, final LocalDateTime time) {
        validateNewToken(newToken, time);
        token = newToken;
    }

    private void validateNewToken(final String newToken, final LocalDateTime time) {
        if (newToken == null || newToken.isBlank()) {
            throw new IllegalArgumentException("refresh token 이 유효하지 않습니다.");
        }

        if (newToken.equals(token)) {
            throw new IllegalArgumentException("refresh token 이 null 이나 공백으로 입력되었습니다.");
        }

        if (time.isAfter(expirationTime)) {
            throw new JwtException("Refresh 토큰이 만료되었습니다.");
        }
    }
}
