package hamkke.board.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UserResponse {

    private final String loginId;

    @JsonCreator
    public UserResponse(final String loginId) {
        this.loginId = loginId;
    }
}
