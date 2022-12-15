package hamkke.board.service.dto;

import lombok.Getter;

@Getter
public class LoginServiceResponseDto {

    private final String token;
    private final Long userId;
    private final String alias;

    public LoginServiceResponseDto(final String token, final Long userId, final String alias) {
        this.token = token;
        this.userId = userId;
        this.alias = alias;
    }
}
