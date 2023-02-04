package hamkke.board.service;

import hamkke.board.service.dto.user.UniqueConstraintCondition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueConstraintConditionTest {

    @Mock
    private DataIntegrityViolationException dataIntegrityViolationException;

    @ParameterizedTest
    @MethodSource("dataIntegrityViolationExceptionSituationParameterProvider")
    @DisplayName("DataIntegrityViolationException 예외를 받으면 loginId 나 alias 상황에 맞는 UniqueConstraintCondition 을 반환한다.")
    void matchCondition(final String exceptionMessage, final UniqueConstraintCondition expect) {
        //given
        when(dataIntegrityViolationException.getMessage()).thenReturn(exceptionMessage);

        //when
        UniqueConstraintCondition uniqueConstraintCondition = UniqueConstraintCondition.matchCondition(dataIntegrityViolationException);

        //then
        assertThat(uniqueConstraintCondition).isSameAs(expect);
    }

    private static Stream<Arguments> dataIntegrityViolationExceptionSituationParameterProvider() {
        return Stream.of(
                Arguments.of("UK.login_id_unique~~testMessage", UniqueConstraintCondition.LOGIN_ID),
                Arguments.of("UK.alias_unique~~testMessage", UniqueConstraintCondition.ALIAS)
        );
    }

    @Test
    @DisplayName("DataIntegrityViolationException 예외를 받고, 세부 예외로 핸들링 할수 없다면 처리할 수 없다는 예외를 반환한다.")
    void matchNothing() {
        //given
        when(dataIntegrityViolationException.getMessage()).thenReturn("none_match_unique~~testMessage");

        //when, when
        assertThatThrownBy(() -> UniqueConstraintCondition.matchCondition(dataIntegrityViolationException))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 제약 조건이 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("uniqueConstraintConditionProvider")
    @DisplayName("uniqueConstraintCondition 에 해당하는 메세지를 가진 예외를 생성하여 반환한다.")
    void generateException(final UniqueConstraintCondition uniqueConstraintCondition, final String expect) {
        //given
        SoftAssertions softly = new SoftAssertions();

        //when
        IllegalStateException actual = uniqueConstraintCondition.generateException();

        //then
        softly.assertThat(actual).isInstanceOf(IllegalStateException.class);
        softly.assertThat(actual).hasMessage(expect);
        softly.assertAll();
    }

    private static Stream<Arguments> uniqueConstraintConditionProvider() {
        return Stream.of(
                Arguments.of(UniqueConstraintCondition.LOGIN_ID, "이미 존재하는 ID 입니다."),
                Arguments.of(UniqueConstraintCondition.ALIAS, "이미 존재하는 별명 입니다."));
    }
}
