package hamkke.board.model.user;

import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

@EqualsAndHashCode
public class Alias {

    private static final String ALIAS_REGEX = "^[a-z0-9가-힣]{1,8}$";
    private static final Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_REGEX);

    private final String value;

    public Alias(final String alias) {
        validateByAliasPattern(alias);
        value = alias;
    }

    private void validateByAliasPattern(final String alias) {
        if (!ALIAS_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException("별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상 8자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
