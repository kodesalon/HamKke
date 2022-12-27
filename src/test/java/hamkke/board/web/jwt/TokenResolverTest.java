package hamkke.board.web.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenResolverTest {
    private static final String SECRET_KEY = "ffefcb16382a3ae31cd9ca2d8a98eb6142d4a24c6173150409aa55d9f58976026524aa77129aa6339c390e8355664941117e2acb322939205fa531f4e08a4a0c";
    private static final long TOKEN_VALIDITY_IN_SECONDS = 1800L;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private final TokenResolver tokenResolver = new TokenResolver(SECRET_KEY, TOKEN_VALIDITY_IN_SECONDS);

    @Test
    @DisplayName("userId 를 입력받아 토큰을 생성한다.")
    void createToken() {
        //given
        Long userId = 1L;

        //when
        String token = tokenResolver.createToken(userId);

        //then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("토큰이 가지고 있는 userId를 반환한다.")
    void getUserId() {
        //given
        String token = tokenResolver.createToken(1L);
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
        String token = tokenResolver.createToken(1L);

        //when
        boolean actual = tokenResolver.validateToken(token);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("토큰의 서명이 유효한지 검사한다. 유효하지 않는 경우 예외를 반환한다.")
    void validateTokenWhenFailedByInvalidSignature() {
        //given
        String invalidSecretKey = "invalidKeyqebrhs3dalf7h2l4358i6hg6ase5rtret1";
        Key invalidKey = generateKey(invalidSecretKey);
        Date testDate = new Date(System.currentTimeMillis() + Duration.ofMinutes(5).toMillis());
        String invalidToken = Jwts.builder()
                .setExpiration(testDate)
                .signWith(invalidKey, SIGNATURE_ALGORITHM)
                .compact();

        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(invalidToken)).isInstanceOf(JwtException.class)
                .hasMessage("토큰이 유효하지 않습니다.");
    }

    private Key generateKey(final String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SIGNATURE_ALGORITHM.getJcaName());
    }

    @Test
    @DisplayName("토큰의 기한이 만료되었는지 검사한다. 만료된 경우 경우 예외를 반환한다.")
    void validateTokenWhenFailed() {
        //given
        Key key = generateKey(SECRET_KEY);

        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() - Duration.ofSeconds(1).toMillis()))
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();

        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(token)).isInstanceOf(JwtException.class)
                .hasMessage("토큰의 유효기간이 만료되었습니다.");
    }

    @Test
    @DisplayName("토큰의 구조가 잘못된 경우 예외를 반환한다.")
    void validateTokenWhenMalformed() {
        //given
        String token = "MalformedToken";

        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(token)).isInstanceOf(JwtException.class)
                .hasMessage("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("토큰의 서명이 없는 토큰의 경우 예외를 발생한다.")
    void validateTokenWhenUnsupported() {
        //given
        String unSupportedJwtToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_IN_SECONDS))
                .compact();

        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(unSupportedJwtToken)).isInstanceOf(JwtException.class)
                .hasMessage("지원하지 않는 토큰입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰의 입력이 잘못된 경우(토큰이 null 이거나 공백이 들어온 경우) 예외를 반환한다.")
    void validateTokenWhenIllegalArgument(final String illegalArgument) {
        //when, then
        assertThatThrownBy(() -> tokenResolver.validateToken(illegalArgument)).isInstanceOf(JwtException.class)
                .hasMessage("JWT 토큰이 잘못되었습니다.");
    }
}
