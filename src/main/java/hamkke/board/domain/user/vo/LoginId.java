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
public class LoginId {

    private static final String USER_ID_REGEX = "^[a-z]+[a-z 0-9]{5,19}$";
    private static final Pattern USER_ID_PATTERN = Pattern.compile(USER_ID_REGEX);

    @Column(name = "login_id")
    private String value;

    public LoginId(final String value) {
        validateByUserIdPattern(value);
        this.value = value;
    }

    private void validateByUserIdPattern(final String loginId) {
        if (!USER_ID_PATTERN.matcher(loginId).matches()) {
            throw new IllegalArgumentException("유저의 아이디는 공백을 제외하고, 영문자(소문자)로 시작한 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
