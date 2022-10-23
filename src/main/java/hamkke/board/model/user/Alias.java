package hamkke.board.model.user;

import java.util.Objects;
import java.util.regex.Pattern;

public class Alias {

    private static final String ALIAS_PATTERN = "^[a-z0-9가-힣]{1,8}$";

    private final String value;

    public Alias(final String inputAlias) {
        validateByAliasPattern(inputAlias);
        value = inputAlias;
    }

    private void validateByAliasPattern(final String inputAlias) {
        if (!Pattern.matches(ALIAS_PATTERN, inputAlias)) {
            throw new IllegalArgumentException("별칭은 특수문자와 영어(대문자)를 제외하여 8자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alias alias = (Alias) o;
        return Objects.equals(value, alias.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
