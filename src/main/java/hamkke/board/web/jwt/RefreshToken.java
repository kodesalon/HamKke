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

    public void switchToken(final String newToken) {
        if (newToken.isEmpty()) {
            throw new IllegalArgumentException("refresh token 이 유효하지 않습니다.");
        }
        token = newToken;
    }

    public void checkExpirationTime(final LocalDateTime time) {
        if (time.isAfter(expirationTime)) {
            throw new JwtException("Refresh 토큰이 만료되었습니다.");
        }
    }
}
