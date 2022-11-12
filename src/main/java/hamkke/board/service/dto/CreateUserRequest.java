package hamkke.board.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private final String loginId;
    private final String alias;
    private final String password;

    public CreateUserRequest(final String loginId, final String password, final String alias) {
        this.loginId = loginId;
        this.password = password;
        this.alias = alias;
    }
}
