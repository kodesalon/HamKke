package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("DTO 를 입력받아 회원가입을 한 후 userId를 반환한다.")
    void join() {
        //given
        given(user.getId()).willReturn(1L);
        given(userRepository.save(any(User.class))).willReturn(user);

        //when
        userService.join(new CreateUserRequest("apple123", "apple123!!", "삼다수"));

        //then
        verify(userRepository, times(1)).findUserByAlias(any(Alias.class));
        verify(userRepository, times(1)).findUserByLoginId(any(LoginId.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 loginId 를 입력받은 경우 예외를 반환한다.")
    void validateLoginIdDuplication() {
        //given
        CreateUserRequest duplicatedCreateUserRequest = new CreateUserRequest("apple123", "apple123!!", "삼다수");
        given(userRepository.findUserByLoginId(any(LoginId.class))).willReturn(Optional.of(user));

        //when, then
        assertThatThrownBy(() -> userService.join(duplicatedCreateUserRequest)).isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 ID는 이미 사용중 입니다.");
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 alias 를 입력받은 경우 예외를 반환한다.")
    void validateAliasDuplication() {
        //given
        CreateUserRequest duplicatedCreateUserRequest = new CreateUserRequest("apple123", "apple123!!", "삼다수");
        given(userRepository.findUserByAlias(any(Alias.class))).willReturn(Optional.of(user));

        //when, then
        assertThatThrownBy(() -> userService.join(duplicatedCreateUserRequest)).isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 별명은 이미 사용중 입니다.");
    }
}
