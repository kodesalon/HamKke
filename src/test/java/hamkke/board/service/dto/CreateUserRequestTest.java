package hamkke.board.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CreateUserRequestTest {

    @Test
    @DisplayName("아아디, 패스워드, 별명을 반환한다.")
    void getter() {
        //given
        CreateUserRequest createUserRequest = new CreateUserRequest("apple123", "apple123!!", "삼다수");

        //when
        String loginId = createUserRequest.getLoginId();
        String alias = createUserRequest.getAlias();
        String password = createUserRequest.getPassword();

        //then
        assertAll(
                () -> assertThat(loginId).isEqualTo("apple123"),
                () -> assertThat(password).isEqualTo("apple123!!"),
                () -> assertThat(alias).isEqualTo("삼다수")
        );
    }
}
