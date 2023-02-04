package hamkke.board.service.dto.user;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public enum UniqueConstraintCondition {

    LOGIN_ID("login_id_unique", () -> new IllegalStateException("이미 존재하는 ID 입니다.")),
    ALIAS("alias_unique", () -> new IllegalStateException("이미 존재하는 별명 입니다."));

    private static final String NONE_MATCHING_MESSAGE = "해당하는 제약 조건이 없습니다.";
    private final String columName;
    private final Supplier<IllegalStateException> supplier;

    UniqueConstraintCondition(final String columName, final Supplier<IllegalStateException> supplier) {
        this.columName = columName;
        this.supplier = supplier;
    }

    public static UniqueConstraintCondition matchCondition(final DataIntegrityViolationException exception) {
        String exceptionMessage = exception.getMessage();
        return Arrays.stream(UniqueConstraintCondition.values())
                .filter(condition -> exceptionMessage != null && exceptionMessage.contains(condition.columName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NONE_MATCHING_MESSAGE));
    }

    public IllegalStateException generateException() {
        return supplier.get();
    }
}
