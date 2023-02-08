package hamkke.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamkke.board.service.AuthenticationService;
import hamkke.board.service.dto.JwtTokenResponse;
import hamkke.board.service.dto.LoginRequest;
import hamkke.board.service.dto.LoginResponse;
import hamkke.board.service.dto.RefreshTokenRequest;
import hamkke.board.web.jwt.TokenResolver;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenResolver tokenResolver;

    @MockBean
    private AuthenticationService authenticationService;

    private final LoginRequest loginRequest = new LoginRequest("apple123", "apple123!!");
    private final LoginResponse loginResponse = new LoginResponse("test Access Token", "test Refresh Token", "apple123", "삼다수");

    @Test
    @DisplayName("로그인 시, 입력받은 loginId 와 password 가 일치하면 , accessToken, refreshToken 토큰값을 각각 Authorization 헤더에 담고 loginResponse dto 와 HTTP 200 상태코드를 반환한다.")
    void login() throws Exception {
        //given
        when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        //when
        ResultActions actual = mockMvc.perform(post("/api/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        actual.andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value("apple123"))
                .andExpect(jsonPath("$.alias").value("삼다수"))
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @DisplayName("로그인 시, 입력받은 loginId 와 password 가 일치하지 않으면 예외를 DTO 에 담아 반환하고 , HTTP 400 상태코드를 반환한다.")
    void failLogin() throws Exception {
        //given
        when(authenticationService.login(any(LoginRequest.class))).thenThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."));

        //when
        ResultActions actual = mockMvc.perform(post("/api/authentication/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("토큰 재발행 시, 입력 받은 RefreshToken 이 유효한지 검사 한 후, 유효한 경우 생성한 AccessToken 과 RefreshToken 을 담은 DTO 를 반환한다. " +
            "HTTP 200 상태코드를 반환한다.")
    void reissueToken() throws Exception {
        //given
        JwtTokenResponse jwtTokenResponse = mock(JwtTokenResponse.class);
        when(jwtTokenResponse.getAccessToken()).thenReturn("new Access Token");
        when(jwtTokenResponse.getRefreshToken()).thenReturn("new Refresh Token");
        when(authenticationService.reissue(any(RefreshTokenRequest.class))).thenReturn(jwtTokenResponse);

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("Refresh Token");

        //when
        ResultActions actual = mockMvc.perform(post("/api/authentication/reissueToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)));

        //then
        actual.andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @DisplayName("토큰 재발행 시, 입력 받은 RefreshToken 존재하지 않는 경우 예외를 생성하고, HTTP 400 상태코드를 반환한다.")
    void failReissueToken() throws Exception {
        //given
        when(authenticationService.reissue(any(RefreshTokenRequest.class))).thenThrow(new JwtException("Refresh Token 이 존재하지 않습니다."));

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("Refresh Token");

        //when
        ResultActions actual = mockMvc.perform(post("/api/authentication/reissueToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Refresh Token 이 존재하지 않습니다."));
    }
}
