package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private final String loginId;
    private final String password;

    public LoginRequest(final String loginId, final String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
