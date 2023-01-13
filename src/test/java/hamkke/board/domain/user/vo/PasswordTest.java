package hamkke.board.domain.user.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PasswordTest {

    @ParameterizedTest
    @DisplayName("비밀번호를 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"apple123!", "^^abc1234", "q1w@e3r$t5y^"})
    void create(final String input) {
        //when, then
        assertThatCode(() -> new Password(input)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야합니다." +
            "그렇지 않은 경우 예외를 발생한다.")
    @ValueSource(strings = {"apple1!", "0123456789012345a1!", "appleBanana1", "appleBanana!", "apple012345?", "     apple", " ", ""})
    void validate(final String inputPassword) {
        //when, then
        assertThatThrownBy(() -> new Password(inputPassword)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야합니다.");
    }

    @Test
    @DisplayName("비밀번호를 반환한다.")
    void getValue() {
        //given
        Password password = new Password("apple12345!@");

        //when
        String actual = password.getValue();

        //then
        assertThat(actual).isEqualTo("apple12345!@");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외를 반환한다.")
    void checkPassword() {
        //given
        Password password = new Password("apple123!!");
        String otherPassword = "banana123!!";

        //when, then
        assertThatThrownBy(() -> password.checkPassword(otherPassword)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
