package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    private final String refreshToken;

    public RefreshTokenRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
