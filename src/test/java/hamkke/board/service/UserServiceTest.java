package hamkke.board.service;

import hamkke.board.config.JpaAuditingConfiguration;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(JpaAuditingConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private CreateUserRequest generateCreateUserRequest() {
        return new CreateUserRequest("apple123", "apple123!", "삼다수");
    }

    @Test
    @DisplayName("회원 가입 성공 시, 회원의 식별 id를 반환한다.")
    void join() {
        //given
        userService = new UserService(userRepository);
        CreateUserRequest createUserRequest = generateCreateUserRequest();

        //when
        Long joinedId = userService.join(createUserRequest);

        //then
        assertThat(joinedId).isInstanceOfAny(Long.class);
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 loginId 를 입력받은 경우 예외를 반환한다.")
    void validateLoginIdDuplication() {
        //given
        userService = new UserService(userRepository);
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        CreateUserRequest duplicatedUserRequest =  new CreateUserRequest("apple123", "banana123!", "삼다수123");
        userService.join(createUserRequest);

        //when, then
        assertThatThrownBy(() -> userService.join(duplicatedUserRequest)).isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 ID는 이미 사용중 입니다.");
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 alias 를 입력받은 경우 예외를 반환한다.")
    void validateAliasDuplication() {
        //given
        userService = new UserService(userRepository);
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        CreateUserRequest duplicatedUserRequest =  new CreateUserRequest("banana123", "banana123!", "삼다수");
        userService.join(createUserRequest);

        //when, then
        assertThatThrownBy(() -> userService.join(duplicatedUserRequest)).isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 별명은 이미 사용중 입니다.");
    }
}
