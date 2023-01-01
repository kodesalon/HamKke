package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.repository.RefreshTokenRepository;
import hamkke.board.service.dto.JwtTokenResponse;
import hamkke.board.service.dto.LoginRequest;
import hamkke.board.service.dto.LoginResponse;
import hamkke.board.service.dto.RefreshTokenRequest;
import hamkke.board.web.jwt.RefreshToken;
import hamkke.board.web.jwt.TokenResolver;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenResolver tokenResolver;

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        User user = userService.findByLoginId(loginId);
        user.checkPassword(loginRequest.getPassword());
        JwtTokenResponse jwtTokenResponse = issueJwtToken(user);
        return new LoginResponse(jwtTokenResponse.getAccessToken(), jwtTokenResponse.getRefreshToken(), user.getId(), user.getAlias().getValue());
    }

    private JwtTokenResponse issueJwtToken(final User user) {
        String newRefreshToken = UUID.randomUUID().toString();
        String newAccessToken = tokenResolver.createToken(user.getId());
        issue(user, newRefreshToken, newAccessToken);
        return new JwtTokenResponse(newAccessToken, newRefreshToken);
    }

    private void issue(final User user, final String newRefreshToken, final String newAccessToken) {
        refreshTokenRepository.findByUserLoginIdValue(user.getLoginId().getValue())
                .ifPresentOrElse(
                        existRefreshToken -> {
                            existRefreshToken.switchToken(newRefreshToken);
                            log.info("JWT 토큰 재발행 - loginId : {}, Alias : {}, Access Token : {}, RefreshToken : {} ", user.getLoginId(), user.getAlias(), newAccessToken, newRefreshToken);
                        },
                        () -> {
                            RefreshToken refreshToken = new RefreshToken(user, newRefreshToken, LocalDateTime.now());
                            refreshTokenRepository.save(refreshToken);
                            log.info("JWT 토큰 발행 - loginId : {}, Alias : {}, Access Token : {}, RefreshToken : {}", user.getLoginId(), user.getAlias(), newAccessToken, newRefreshToken);
                        }
                );
    }

    @Transactional
    public JwtTokenResponse reissue(final RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = checkRefreshToken(requestRefreshToken);
        String newRefreshToken = UUID.randomUUID().toString();
        refreshToken.switchToken(newRefreshToken);
        return new JwtTokenResponse(tokenResolver.createToken(refreshToken.getUser().getId()), newRefreshToken);
    }

    private RefreshToken checkRefreshToken(final String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(
                        () -> new JwtException("Refresh Token 이 존재하지 않습니다.")
                );
        refreshToken.checkExpirationTime(LocalDateTime.now());
        return refreshToken;
    }
}
