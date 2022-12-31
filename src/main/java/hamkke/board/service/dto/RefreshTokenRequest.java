package hamkke.board.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenRequest {

    private String refreshToken;

    public RefreshTokenRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
