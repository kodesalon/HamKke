package hamkke.board.service;

import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 가입 성공 시, 회원의 식별 id를 반환한다.")
    void join() {
        //given
        UserService userService = new UserService(userRepository);
        CreateUserRequest createUserRequest = new CreateUserRequest("apple123", "apple123!", "삼다수");

        //when
        Long joinedId = userService.join(createUserRequest);

        //then
        assertThat(joinedId).isInstanceOfAny(Long.class);
    }
}
