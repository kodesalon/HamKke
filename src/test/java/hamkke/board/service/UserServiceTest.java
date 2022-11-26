package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.of;
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
    @DisplayName("DTO 를 입력받아 회원가입을 한다.")
    void join() {
        //given
        when(user.getId()).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        userService.join(generateCreateUserRequest());

        //then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @ParameterizedTest
    @MethodSource("createUserRequestParameterProvider")
    @DisplayName("회원 가입 시, 중복된 loginId 나 alias 를 입력받은 경우 예외를 반환한다.")
    void validateLoginIdDuplication(final CreateUserRequest duplicated) {
        //given
        when(user.getId()).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(user)
                .thenThrow(new IllegalStateException("이미 사용중인 loginId 혹은 alias 입니다."));

        CreateUserRequest createUserRequest = generateCreateUserRequest();
        userService.join(createUserRequest);

        //when, then
        assertThatThrownBy(() -> userService.join(duplicated)).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 사용중인 loginId 혹은 alias 입니다.");
    }

    static Stream<Arguments> createUserRequestParameterProvider() {
        return Stream.of(
                of(new CreateUserRequest("apple123", "apple123!!", "아이시스")),
                of(new CreateUserRequest("banana123", "apple123!!", "삼다수"))
        );
    }
}
