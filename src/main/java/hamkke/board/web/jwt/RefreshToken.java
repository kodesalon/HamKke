package hamkke.board.web.jwt;

import hamkke.board.domain.user.User;
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
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    public RefreshToken(final User user, final String token, final LocalDateTime expirationTime) {
        this.user = user;
        this.token = token;
        this.expirationTime = expirationTime;
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
