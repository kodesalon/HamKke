package hamkke.board.service.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateUserRequest {

    @NotNull
    private final String loginId;

    @NotNull
    private final String alias;

    @NotNull
    private final String password;

    public CreateUserRequest(final String loginId, final String password, final String alias) {
        this.loginId = loginId;
        this.password = password;
        this.alias = alias;
    }
}
