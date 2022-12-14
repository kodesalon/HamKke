package hamkke.board.web.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;


class TokenResolverTest {

    private TokenResolver tokenResolver;

    private String createJwtToken() {
        Long userId = 1L;
        return tokenResolver.createToken(userId);
    }

    @BeforeEach
    void setUp() {
        tokenResolver = new TokenResolver();
    }

    @Test
    @DisplayName("userId 를 입력받아 토큰을 생성한다.")
    void createToken() {
        //given
        Long userId = 1L;

        //when, then
        assertThatCode(() -> tokenResolver.createToken(userId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("토큰이 가지고 있는 userId를 반환한다.")
    void getUserId() {
        //given
        String token = createJwtToken();
        Long expectUserId = 1L;

        //when
        Long actual = tokenResolver.getUserId(token);

        //then
        assertThat(actual).isEqualTo(expectUserId);
    }

    @Test
    @DisplayName("토큰의 서명이 유효한지 검사한다. 유효한 경우 true 를 반환한다.")
    void validateToken() {
        //given
        String token = createJwtToken();

        //when
        boolean actual = tokenResolver.validateToken(token);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("토큰의 서명이 유효한지 검사한다. 유효하지 않는 경우 예외를 반환한다.")
    void validateTokenWhenFailedByInvalidSignature() {
        //given
        String invalidSecretKey = "invalidKey";
        byte[] invalidSecretKeyBytes = invalidSecretKey.getBytes();
        Key invalidKey = new SecretKeySpec(invalidSecretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
        Date testDate = new Date(System.currentTimeMillis() + Duration.ofMinutes(5).toMillis());

        String invalidToken = Jwts.builder()
                .setExpiration(testDate)
                .signWith(SignatureAlgorithm.HS256, invalidKey)
                .compact();

        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(invalidToken)).isInstanceOf(JwtException.class)
                .hasMessage("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("토큰의 기한이 만료되었는지 검사한다. 만료된 경우 경우 예외를 반환한다.")
    void validateTokenWhenFailed() {
        //given
        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() - Duration.ofSeconds(1).toMillis()))
                .signWith(SignatureAlgorithm.HS256, "password1234")
                .compact();

        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(token)).isInstanceOf(JwtException.class)
                .hasMessage("토큰의 유효기간이 만료되었습니다.");
    }
}
