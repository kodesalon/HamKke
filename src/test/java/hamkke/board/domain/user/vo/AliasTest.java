package hamkke.board.domain.user.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class AliasTest {

    @ParameterizedTest
    @DisplayName("별칭을 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"apple12", "감자", "777", "감자3"})
    void create(final String input) {
        //when, then
        assertThatCode(() -> new Alias(input)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("별칭은 특수문자와 영어(대문자)를 제외하여 8자 이하여야 합니다. 그렇지 않은 경우 예외를 발생한다.")
    @ValueSource(strings = {"!apple", "APPLE", "가나다라마바사아자","a", " ", ""})
    void validateAlias(final String inputAlias) {
        //when, then
        assertThatThrownBy(() -> new Alias(inputAlias)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("별칭은 특수문자와 영어(대문자)를 제외하여 2자 이상, 8자 이하여야 합니다.");
    }

    @Test
    @DisplayName("별칭을 반환한다.")
    void getValue() {
        //given
        String inputAlias = "사과왕77";
        Alias alias = new Alias(inputAlias);

        //when
        String actual = alias.getValue();

        //then
        assertThat(actual).isEqualTo("사과왕77");
    }
}
