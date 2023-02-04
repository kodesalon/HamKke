package hamkke.board.service.dto.authentication.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class LoginResponse {

    @JsonIgnore
    private final String accessToken;

    @JsonIgnore
    private final String refreshToken;

    private final String loginId;
    private final String alias;

    public LoginResponse(final String accessToken, final String refreshToken, final String loginId, final String alias) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.loginId = loginId;
        this.alias = alias;
    }
}
