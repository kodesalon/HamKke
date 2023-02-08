package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.repository.RefreshTokenRepository;
import hamkke.board.service.dto.JwtTokenResponse;
import hamkke.board.service.dto.LoginRequest;
import hamkke.board.service.dto.LoginResponse;
import hamkke.board.service.dto.RefreshTokenRequest;
import hamkke.board.web.jwt.RefreshToken;
import hamkke.board.web.jwt.TokenResolver;
import io.jsonwebtoken.JwtException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

    @Mock
    private TokenResolver tokenResolver;


    @InjectMocks
    private AuthenticationService authenticationService;

    private final SoftAssertions softly = new SoftAssertions();

    @Test
    @DisplayName("회원의 LoginId, 비밀번호를 입력받고 refresh 토큰이 없다면, Refresh 토큰을 생성한 후, 로그인 정보를 DTO 에 담아 반환한다.")
    void login() {
        //given
        User user = mock(User.class);
        when(user.getLoginId()).thenReturn(new LoginId("apple123"));
        when(user.getAlias()).thenReturn(new Alias("삼다수"));
        when(userService.findByLoginId(anyString())).thenReturn(user);
        when(tokenResolver.createToken(any())).thenReturn("test Access Token");

        LoginRequest loginRequest = new LoginRequest("apple123", "apple123!!");

        //when
        LoginResponse loginResponse = authenticationService.login(loginRequest);

        //then
        softly.assertThat(loginResponse.getAccessToken()).isNotEmpty();
        softly.assertThat(loginResponse.getRefreshToken()).isNotEmpty();
        verify(refreshTokenRepository, times(1)).save(any());
        verify(userService, times(1)).findByLoginId(anyString());
        softly.assertAll();
    }

    @Test
    @DisplayName("회원의 LoginId, 비밀번호를 입력받고 refresh 토큰이 있다면, Refresh 재생성하여 교체한 후, 로그인 정보를 DTO 에 담아 반환한다.")
    void loginWhenExistingRefreshToken() {
        //given
        User user = mock(User.class);
        when(user.getLoginId()).thenReturn(new LoginId("apple123"));
        when(user.getAlias()).thenReturn(new Alias("삼다수"));
        when(userService.findByLoginId(anyString())).thenReturn(user);
        when(tokenResolver.createToken(any())).thenReturn("test Access Token");
        RefreshToken refreshToken = mock(RefreshToken.class);
        when(refreshTokenRepository.findByLoginIdValue(anyString())).thenReturn(Optional.of(refreshToken));

        LoginRequest loginRequest = new LoginRequest("apple123", "apple123!!");

        //when
        LoginResponse loginResponse = authenticationService.login(loginRequest);

        //then
        softly.assertThat(loginResponse.getAccessToken()).isNotEmpty();
        softly.assertThat(loginResponse.getRefreshToken()).isNotEmpty();
        verify(userService, times(1)).findByLoginId(anyString());
        verify(refreshToken, times(1)).switchToken(anyString(),any(LocalDateTime.class));
        softly.assertAll();
    }

    @Test
    @DisplayName("로그인 요청 시, 존재하지 않는 LoginId 일 경우, 예외를 반환한다.")
    void failedLoginWhenNotExistLoginId() {
        //given
        when(userService.findByLoginId(anyString())).thenThrow(new IllegalArgumentException("존재하지 않는 ID 입니다."));

        LoginRequest loginRequest = new LoginRequest("apple123", "apple123!!");

        //when, then
        assertThatThrownBy(() -> authenticationService.login(loginRequest)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 ID 입니다.");
    }

    @Test
    @DisplayName("로그인 요청 시, 입력받은 loginId 와 password 가 일치하지 않은 경우 예외를 반환한다.")
    void loginFailedByNotMatchingPassword() {
        //given
        User user = mock(User.class);
        doThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다.")).when(user)
                .checkPassword(anyString());
        when(userService.findByLoginId(anyString())).thenReturn(user);

        LoginRequest loginRequest = new LoginRequest("apple123", "apple123!!");

        //when, then
        assertThatThrownBy(() -> authenticationService.login(loginRequest)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("RefreshToken 을 입력 받아 유효한지 확인 하고, 유효한 경우 AccessToken 과 RefreshToken 을 재발급 하여 DTO 에 담아 반환한다.")
    void reissueAccessTokenAndRefreshToken() {
        //given
        RefreshToken refreshToken = mock(RefreshToken.class);
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(refreshToken.getLoginId()).thenReturn(new LoginId("apple123"));
        when(userService.findByLoginId(anyString())).thenReturn(new User("apple123","apple123!!","삼다수"));
        when(tokenResolver.createToken(anyString())).thenReturn("test access Token");

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refresh Token");

        //when
        JwtTokenResponse actual = authenticationService.reissue(refreshTokenRequest);

        //then
        softly.assertThat(actual.getAccessToken()).isNotEmpty();
        softly.assertThat(actual.getRefreshToken()).isNotEmpty();
        verify(tokenResolver, times(1)).createToken(any());
        verify(refreshToken, times(1)).switchToken(anyString(),any(LocalDateTime.class));
        softly.assertAll();
    }

    @Test
    @DisplayName("RefreshToken 을 입력 받아 유효한지 확인 하고, 유효하지 않은 경우 예외를 반환한다.")
    void  failedReissueAccessTokenAndRefreshToken() {
        //given
        when(refreshTokenRepository.findByToken(anyString())).thenThrow(new JwtException("Refresh Token 이 존재하지 않습니다."));

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refresh Token");

        //when, then
        assertThatThrownBy(() -> authenticationService.reissue(refreshTokenRequest)).isInstanceOf(JwtException.class)
                .hasMessage("Refresh Token 이 존재하지 않습니다.");
    }
}
