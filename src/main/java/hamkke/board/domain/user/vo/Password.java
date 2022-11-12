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
public class Password {

    private static final String PASSWORD_REGEX = "^([a-z0-9!@#$%^&*_-]){8,18}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Column(name = "password")
    private String value;

    public Password(final String value) {
        validateByPasswordPattern(value);
        this.value = value;
    }

    private void validateByPasswordPattern(final String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
