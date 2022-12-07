package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.domain.user.vo.Password;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import hamkke.board.service.dto.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest generateCreateUserRequest() {
        return new CreateUserRequest("apple123", "apple123!!", "아이시스");
    }

    @Test
    @DisplayName("DTO 를 입력받아 회원가입을 한후, id 값을 반화한다.")
    void join() {
        //given
        when(user.getId()).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        Long actual = userService.join(generateCreateUserRequest());

        //then
        assertThat(actual).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 loginId 를 입력받은 경우 예외를 반환한다.")
    void validateLoginIdDuplication() {
        //given
        when(user.getId()).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(user)
                .thenThrow(new IllegalStateException("이미 존재하는 ID 입니다."));

        CreateUserRequest createUserRequest = generateCreateUserRequest();
        CreateUserRequest duplicated = new CreateUserRequest("apple123", "apple123!!", "삼다수");
        userService.join(createUserRequest);

        //when, then
        assertThatThrownBy(() -> userService.join(duplicated)).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 ID 입니다.");
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 alias 를 입력받은 경우 예외를 반환한다.")
    void validateAliasDuplication() {
        //given
        when(user.getId()).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(user)
                .thenThrow(new IllegalStateException("이미 존재하는 별명 입니다."));

        CreateUserRequest createUserRequest = generateCreateUserRequest();
        CreateUserRequest duplicated = new CreateUserRequest("banana123", "apple123!!", "아이시스");
        userService.join(createUserRequest);

        //when, then
        assertThatThrownBy(() -> userService.join(duplicated)).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 별명 입니다.");
    }

    @Test
    @DisplayName("로그인 시, loginId 와 password 가 일치하면, 일치한 User 를 반환한다.")
    void login() {
        //given
        when(user.getPassword()).thenReturn(new Password("apple123!!"));
        when(userRepository.findUserByLoginId(any(LoginId.class))).thenReturn(Optional.of(user));

        LoginRequest loginRequest = new LoginRequest("apple123", "apple123!!");

        //when
        User loginUser = userService.login(loginRequest);

        //then
        assertThat(loginUser).isEqualTo(user);
    }
}
