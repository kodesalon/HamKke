package hamkke.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, MethodArgumentNotValidException.class,IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(final RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
