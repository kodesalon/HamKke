package hamkke.board.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

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
