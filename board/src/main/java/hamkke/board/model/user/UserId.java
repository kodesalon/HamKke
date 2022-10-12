package hamkke.board.model.user;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserId {

    public static final String USER_ID_PATTERN = "^[a-z]+[a-z 0-9]{5,19}$";

    private final String value;

    public UserId(final String inputUserId) {
        validateByUserIdPattern(inputUserId);
        value = inputUserId;
    }

    private void validateByUserIdPattern(final String inputUserId) {
        if (!Pattern.matches(USER_ID_PATTERN, inputUserId)) {
            throw new IllegalArgumentException("유저의 아이디는 공백을 제외하고, 영문자(소문자)로 시작한 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
