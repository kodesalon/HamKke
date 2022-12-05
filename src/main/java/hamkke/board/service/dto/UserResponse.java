package hamkke.board.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long userId;

    @JsonCreator
    public UserResponse(final Long userId) {
        this.userId = userId;
    }
}
