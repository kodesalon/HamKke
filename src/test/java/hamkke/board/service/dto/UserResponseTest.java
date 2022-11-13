package hamkke.board.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    @DisplayName("user 의 id를 입력받는 DTO 를 생성한다.")
    void createUserResponse() {
        //given
        Long userId = 1L;

        //when
        UserResponse userResponse = new UserResponse(userId);

        //then
        assertThat(userResponse).isEqualTo(new UserResponse(1L));
    }

    @Test
    @DisplayName("user 의 id를 반환한다.")
    void getter() {
        //given
        UserResponse userResponse = new UserResponse(1L);

        //when
        Long userId = userResponse.getUserId();

        //then
        assertThat(userId).isEqualTo(1L);
    }
}
