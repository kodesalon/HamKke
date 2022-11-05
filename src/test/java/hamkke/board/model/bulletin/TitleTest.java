package hamkke.board.model.bulletin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {

    @ParameterizedTest
    @DisplayName("글 제목을 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"title", "안녕하세요", "함께 자라기 프로젝트 할 사람 구합니다."})
    void create(final String input) {
        //when
        Title title = new Title(input);

        //then
        assertThat(title).isEqualTo(new Title(input));
    }

    @ParameterizedTest
    @DisplayName("글 제목은 1글자 이상 25자 이하여야한다.")
    @ValueSource(strings = {"", "Hello!! q this is the sample"})
    void validateSize(final String input) {
        //when, then
        assertThatThrownBy(() -> new Title(input)).isInstanceOf(IllegalArgumentException.class)
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