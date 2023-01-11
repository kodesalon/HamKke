package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long userId;
    private final String alias;

    public LoginResponse(final Long userId, final String alias) {
        this.userId = userId;
        this.alias = alias;
    }
}
