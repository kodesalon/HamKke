package hamkke.board.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class UserResponse {

    private final Long userId;

    public UserResponse(final Long userId) {
        this.userId = userId;
    }
}
