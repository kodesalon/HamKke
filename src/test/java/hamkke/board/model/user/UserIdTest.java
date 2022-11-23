package hamkke.board.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class UserIdTest {

    @ParameterizedTest
    @DisplayName("유저 아이디를 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"apple1", "a1234567890123456789", "a12345b23"})
    void create(final String input) {
        //when
        UserId userId = new UserId(input);

        //then
        assertThat(userId).isEqualTo(new UserId(input));
    }

    @ParameterizedTest
    @DisplayName("유저의 아이디는 공백 제외 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야한다." +
            "그렇지 않은 경우 예외를 발생시킨다.")
    @ValueSource(strings = {"1apple", "_apple", " apple", "apple", "a12345678901234567890", "apple!", "@apple", "apple_hi", "", " "})
    void validateUserId(final String input) {
        //when, then
        assertThatThrownBy(() -> new UserId(input)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유저의 아이디는 공백을 제외하고, 영문자(소문자)로 시작한 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야합니다.");
    }

    @Test
    @DisplayName("유저 아이디를 반환하다.")
    void getValue() {
        //given
        UserId apple = new UserId("banana12");

        //when
        String actual = apple.getValue();

        //then
        assertThat(actual).isEqualTo("banana12");
    }
}
