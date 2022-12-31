package hamkke.board.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class LoginResponse {

    @JsonIgnore
    private final String accessToken;

    @JsonIgnore
    private final String refreshToken;

    private final Long userId;
    private final String alias;

    public LoginResponse(final String accessToken, final String refreshToken, final Long userId, final String alias) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.alias = alias;
    }
}
