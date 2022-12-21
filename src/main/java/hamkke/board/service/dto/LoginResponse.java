package hamkke.board.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class LoginResponse {

    @JsonIgnore
    private final String token;
    private final Long userId;
    private final String alias;

    public LoginResponse(final String token, final Long userId, final String alias) {
        this.token = token;
        this.userId = userId;
        this.alias = alias;
    }
}
