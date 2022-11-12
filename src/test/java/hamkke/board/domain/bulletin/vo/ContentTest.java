package hamkke.board.domain.bulletin.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentTest {

    @ParameterizedTest
    @DisplayName("글 내용을 입력받아 객체를 생성한다.")
    @ValueSource(strings = {"안녕하세요", "1111123", "hello~~~~~"})
    void create(final String input) {
        //when
        Content content = new Content(input);

        //then
        assertThat(content).isEqualTo(new Content(input));
    }

    @ParameterizedTest
    @DisplayName("글 내용은 최대 1000 자여야한다.")
    @ValueSource(ints = {0, 1001})
    void validate(final int repeatCount) {
        //given
        String inputContent = "a".repeat(repeatCount);
        //when, then
        assertThatThrownBy(() -> new Content(inputContent)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("글 내용은 최소 1자 이상, 최대 1000자 입니다.");
    }

    @Test
    @DisplayName("글 내용을 반환한다.")
    void getValue() {
        //given
        Content content = new Content("테스트용 샘플 글 내용입니다.");

        //when
        String actual = content.getValue();

        //then
        assertThat(actual).isEqualTo("테스트용 샘플 글 내용입니다.");
    }
}
