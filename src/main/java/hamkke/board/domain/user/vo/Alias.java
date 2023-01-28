package hamkke.board.domain.user.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Alias {

    private static final String ALIAS_REGEX = "^[a-z0-9가-힣]{2,8}$";
    private static final Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_REGEX);

    @Column(name = "alias")
    private String value;

    public Alias(final String value) {
        validateByAliasPattern(value);
        this.value = value;
    }

    private void validateByAliasPattern(final String alias) {
        if (!ALIAS_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException("별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상, 8자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    public void changeValue(final String newAlias) {
        validateByAliasPattern(newAlias);
        this.value = newAlias;
    }
}
