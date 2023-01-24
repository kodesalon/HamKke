package hamkke.board.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AllExceptionHandler {

    private static final int INDEX_OF_BASIC_ERROR = 0;

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(final RuntimeException e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleOverlapException(final IllegalStateException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, JwtException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(final RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(final BindException e) {
        return ResponseEntity.internalServerError()
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentValidation(final MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(getDefaultErrorMessage(e)));
    }

    private String getDefaultErrorMessage(final MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getAllErrors()
                .get(INDEX_OF_BASIC_ERROR)
                .getDefaultMessage();
    }
}
