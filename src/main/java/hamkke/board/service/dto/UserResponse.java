package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long userId;

    public UserResponse(final Long userId) {
        this.userId = userId;
    }
}
