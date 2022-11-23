package hamkke.board.model.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AliasTest {

    @ParameterizedTest
    @DisplayName("별칭을 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"apple12", "감자", "777", "감자3"})
    void create(final String input) {
        //when
        Alias alias = new Alias(input);

        //then
        assertThat(alias).isEqualTo(new Alias(input));
    }

    @ParameterizedTest
    @DisplayName("별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상 8자 이하여야 한다." +
            "그렇지 않은 경우 예외를 발생시킨다.")
    @ValueSource(strings = {"!apple", "APPLE", "가나다라마바사아자", " ", ""})
    void validateAlias(final String input) {
        //when, then
        assertThatThrownBy(() -> new Alias(input)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상 8자 이하여야 합니다.");
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
