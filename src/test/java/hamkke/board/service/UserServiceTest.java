package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.user.UserService;
import hamkke.board.service.dto.user.request.CreateUserRequest;
import hamkke.board.service.dto.user.request.UserChangeAliasRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

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
    @DisplayName("DTO 를 입력받아 회원가입을 한후, LoginId 값을 반화한다.")
    void join() {
        //given
        when(user.getLoginId()).thenReturn(new LoginId("apple123"));
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        String actual = userService.join(generateCreateUserRequest());

        //then
        assertThat(actual).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 시, 중복된 loginId 를 입력받은 경우 예외를 반환한다.")
    void validateLoginIdDuplication() {
        //given
        when(user.getLoginId()).thenReturn(new LoginId("apple123"));
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
        when(user.getLoginId()).thenReturn(new LoginId("apple123"));
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
    @DisplayName("User 의 Alias 를 변경한다.")
    void changeAlias() {
        //given
        when(userRepository.findByLoginIdValue(anyString())).thenReturn(Optional.of(user));
        String loginId = "apple123";
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("백산수");

        //when
        userService.changeAlias(loginId, userChangeAliasRequest);

        //then
        verify(userRepository, times(1)).findByLoginIdValue(anyString());
        verify(user, times(1)).changeAlias(anyString());
    }

    @Test
    @DisplayName("User 의 alias 변경 시, 요청한 유저가 존재하지 않는다면 예외를 반환한다.")
    void changeAliasFailedByNonexistentUser() {
        //given
        when(userRepository.findByLoginIdValue(anyString())).thenReturn(Optional.empty());

        String loginId = "apple123";
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("새로운별명");

        //when, then
        assertThatThrownBy(() -> userService.changeAlias(loginId, userChangeAliasRequest)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 ID 입니다.");
    }

    @Test
    @DisplayName("User 의 Alias 를 변경 시 이미 존재하는 alias 라면 예외를 반환한다.")
    void changeAliasFailedByDuplicatedAlias() {
        //given
        when(userRepository.findByLoginIdValue(anyString())).thenReturn(Optional.of(user));
        doThrow(new DataIntegrityViolationException("alias_unique ~~~")).when(user)
                .changeAlias(anyString());

        String loginId = "apple123";
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("백산수");

        //when, then
        assertThatThrownBy(() -> userService.changeAlias(loginId, userChangeAliasRequest)).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 별명 입니다.");
    }
}
