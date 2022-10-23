package hamkke.board.model.user;

import java.util.regex.Pattern;

public class Password {

    public static final String PASSWORD_PATTERN = "^([a-z0-9!@#$%^&*_-]){8,18}$";

    private final String value;

    public Password(final String inputPassword) {
        validateByPasswordPattern(inputPassword);
        value = inputPassword;
    }

    private void validateByPasswordPattern(final String inputPassword) {
        if (!Pattern.matches(PASSWORD_PATTERN, inputPassword)) {
            throw new IllegalArgumentException("비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
