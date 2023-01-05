package hamkke.board.web.jwt;

import hamkke.board.domain.user.vo.LoginId;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefreshTokenTest {

    private RefreshToken refreshToken;
    private LocalDateTime expirationTime;

    @BeforeEach
    void setUp() {
        LoginId loginId = new LoginId("apple123");
        expirationTime = LocalDateTime.now();
        refreshToken = new RefreshToken(loginId, "token", expirationTime);
    }

    @Test
    @DisplayName("RefreshToken 값을 반환한다.")
    void getToken() {
        //given
        String expect = "token";

        //when
        String actual = refreshToken.getToken();

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("RefreshToken 값을 새로운 값으로 변경한다.")
    void switchToken() {
        //given
        String newToken = "newToken";

        //when
        refreshToken.switchToken(newToken, LocalDateTime.now().minusSeconds(1));

        //then
        assertThat(refreshToken.getToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("입력받은 시간과 토큰의 만료 시간을 비교하여, 토큰이 만료된 경우 예외를 반환한다.")
    void failedSwitchTokenWithExpiredTimeOver() {
        //given
        LocalDateTime tokenRequestTime = expirationTime.plusSeconds(1);
        String newToken = "newToken";

        //when, then
        assertThatThrownBy(() -> refreshToken.switchToken(newToken ,tokenRequestTime)).isInstanceOf(JwtException.class)
                .hasMessage("Refresh 토큰이 만료되었습니다.");
    }
}
