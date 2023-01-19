package hamkke.board.controller;

import hamkke.board.service.AuthenticationService;
import hamkke.board.service.dto.JwtTokenResponse;
import hamkke.board.service.dto.LoginRequest;
import hamkke.board.service.dto.LoginResponse;
import hamkke.board.service.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok()
                .header("Authorization", loginResponse.getAccessToken(), loginResponse.getRefreshToken())
                .body(loginResponse);
    }

    @PostMapping("/reissueToken")
    public ResponseEntity<JwtTokenResponse> reissueJwtToken(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtTokenResponse jwtTokenResponse = authenticationService.reissue(refreshTokenRequest);
        return ResponseEntity.ok()
                .header("Authorization", jwtTokenResponse.getAccessToken(), jwtTokenResponse.getRefreshToken())
                .build();
    }
}
