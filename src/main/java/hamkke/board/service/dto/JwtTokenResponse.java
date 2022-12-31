package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class JwtTokenResponse {

    private final String accessToken;
    private final String refreshToken;

    public JwtTokenResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
