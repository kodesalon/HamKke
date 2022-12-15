package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String token;
    private final String alias;

    public LoginResponse(final String token, final String alias) {
        this.token = token;
        this.alias = alias;
    }
}
