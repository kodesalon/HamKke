package hamkke.board.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String error;

    public ErrorResponse(final String error) {
        this.error = error;
    }
}
