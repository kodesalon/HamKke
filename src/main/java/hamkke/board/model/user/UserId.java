package hamkke.board.model.user;

import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

@EqualsAndHashCode
public class UserId {

    private static final String USER_ID_REGEX = "^[a-z]+[a-z 0-9]{5,19}$";
    private static final Pattern USER_ID_PATTERN = Pattern.compile(USER_ID_REGEX);

    private final String value;

    public UserId(final String userId) {
        validateByUserIdPattern(userId);
        value = userId;
    }

    private void validateByUserIdPattern(final String userId) {
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
            throw new IllegalArgumentException("유저의 아이디는 공백을 제외하고, 영문자(소문자)로 시작한 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}