package hamkke.board.model.user;

import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

@EqualsAndHashCode
public class Password {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*_-])[A-Za-z\\d!@#$%^&*_-]{8,18}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private final String value;

    public Password(final String password) {
        validateByPasswordPattern(password);
        value = password;
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
