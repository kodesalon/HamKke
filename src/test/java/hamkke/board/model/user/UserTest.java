package hamkke.board.model.user;

import hamkke.board.model.user.vo.Alias;
import hamkke.board.model.user.vo.Password;
import hamkke.board.model.user.vo.UserLoginId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserTest {

    UserLoginId userLoginId = new UserLoginId("apple123");
    Password password = new Password("apple123!!");
    Alias alias = new Alias("삼다수");
    LocalDateTime createdDateTime = LocalDateTime.now();

    @Test
    @DisplayName("유저 아이디, 비밀번호, 별칭, 생성 일자를 반환한다.")
    void getValues() {
        //given
        User user = new User(userLoginId, password, alias, createdDateTime);

        //when, then
        assertAll(
                () -> assertThat(user.getUserLoginId()).isEqualTo(userLoginId),
                () -> assertThat(user.getPassword()).isEqualTo(password),
                () -> assertThat(user.getAlias()).isEqualTo(alias),
                () -> assertThat(user.getCreatedDateTime()).isEqualTo(createdDateTime)
        );
    }
}
