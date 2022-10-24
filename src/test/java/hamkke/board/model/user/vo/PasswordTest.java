package hamkke.board.model.user.vo;

import hamkke.board.model.user.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @ParameterizedTest
    @DisplayName("비밀번호를 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"apple123!", "^^abc1234", "q1w@e3r$t5y^"})
    void create(final String input) {
        //when
        Password password = new Password(input);

        //then
        assertThat(password).isEqualTo(new Password(input));
    }

    @ParameterizedTest
    @DisplayName("비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야한다")
    @ValueSource(strings = {"apple1!", "0123456789012345a1!", "appleBanana1", "appleBanana!", "apple012345?", "     apple"," ", ""})
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
}