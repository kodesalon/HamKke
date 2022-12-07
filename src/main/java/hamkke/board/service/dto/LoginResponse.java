package hamkke.board.service.dto;

import hamkke.board.domain.user.vo.Alias;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long id;
    private final Alias alias;

    public LoginResponse(final Long id, final Alias alias) {
        this.id = id;
        this.alias = alias;
    }
}
