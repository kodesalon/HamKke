package hamkke.board.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class UserIdTest {

    @ParameterizedTest
    @DisplayName("유저의 아이디는 공백 제외 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야합니다.")
    @ValueSource(strings = {"1apple", "_apple", " apple", "apple", "a12345678901234567890", "apple!", "@apple", "apple_hi", "", " "})
    void validateUserId(final String inputUserId) {
        //when, then
        assertThatThrownBy(() -> new UserId(inputUserId))
                .isInstanceOf(IllegalArgumentException.class)
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