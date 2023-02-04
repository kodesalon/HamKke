package hamkke.board.service.dto.authentication;

import hamkke.board.domain.user.User;
import hamkke.board.repository.RefreshTokenRepository;
import hamkke.board.service.dto.authentication.request.LoginRequest;
import hamkke.board.service.dto.authentication.request.RefreshTokenRequest;
import hamkke.board.service.dto.authentication.response.JwtTokenResponse;
import hamkke.board.service.dto.authentication.response.LoginResponse;
import hamkke.board.service.dto.user.UserService;
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
        return new LoginResponse(jwtTokenResponse.getAccessToken(), jwtTokenResponse.getRefreshToken(), user.getLoginId().getValue(), user.getAlias().getValue());
    }

    private JwtTokenResponse issueJwtToken(final User user) {
        String newRefreshToken = UUID.randomUUID().toString();
        String newAccessToken = tokenResolver.createToken(user.getLoginId().getValue());
        issue(user, newRefreshToken, newAccessToken);
        return new JwtTokenResponse(newAccessToken, newRefreshToken);
    }

    private void issue(final User user, final String newRefreshToken, final String newAccessToken) {
        refreshTokenRepository.findByLoginIdValue(user.getLoginId().getValue())
                .ifPresentOrElse(
                        existRefreshToken -> {
                            existRefreshToken.switchToken(newRefreshToken, LocalDateTime.now());
                            log.info("JWT 토큰 재발행 - loginId : {}, Alias : {}, Access Token : {}, RefreshToken : {} ", user.getLoginId(), user.getAlias(), newAccessToken, newRefreshToken);
                        },
                        () -> {
                            RefreshToken refreshToken = new RefreshToken(user.getLoginId(), newRefreshToken, LocalDateTime.now());
                            refreshTokenRepository.save(refreshToken);
                            log.info("JWT 토큰 발행 - loginId : {}, Alias : {}, Access Token : {}, RefreshToken : {}", user.getLoginId(), user.getAlias(), newAccessToken, newRefreshToken);
                        }
                );
    }

    @Transactional
    public JwtTokenResponse reissue(final RefreshTokenRequest request) {
        RefreshToken existRefreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new JwtException("Refresh Token 이 존재하지 않습니다."));
        String newRefreshToken = UUID.randomUUID().toString();
        existRefreshToken.switchToken(newRefreshToken, LocalDateTime.now());
        User user = userService.findByLoginId(existRefreshToken.getLoginId().getValue());
        return new JwtTokenResponse(tokenResolver.createToken(user.getLoginId().getValue()), newRefreshToken);
    }
}
