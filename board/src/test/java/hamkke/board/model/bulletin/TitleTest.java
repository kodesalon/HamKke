package hamkke.board.model.bulletin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @ParameterizedTest
    @DisplayName("글 제목은 1글자 이상 25자 이하여야합니다.")
    @ValueSource(strings = {"", "Hello!! q this is the sample"})
    void validateSize(final String inputTitle) {
        //when, then
        assertThatThrownBy(() -> new Title(inputTitle)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("글 제목은 1글자 이상 25자 이하야합니다.");
    }

    @Test
    @DisplayName("글 제목을 반환한다.")
    void getValue() {
        //given
        String testTitle = "안녕하세요. 테스트용 제목입니다.";
        Title title = new Title(testTitle);

        //when
        String actual = title.getValue();

        //then
        assertThat(actual).isEqualTo(testTitle);

    }
}