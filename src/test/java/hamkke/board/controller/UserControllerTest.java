package hamkke.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamkke.board.service.UserService;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("loginId, password, alias 를 json 으로 전달받아 회원 가입을 하고, HTTP 201 상태 코드를 반환한다.")
    void join() throws Exception {
        //given
        when(userService.join(any(CreateUserRequest.class))).thenReturn(1L);

        CreateUserRequest createUserRequest = new CreateUserRequest("apple123", "apple123!!", "아이폰");

        //when
        ResultActions actual = mockMvc.perform(
                post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)));

        //then
        actual.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("1"));
    }

    @ParameterizedTest
    @MethodSource("createUserRequestParameterProvider")
    @DisplayName("회원가입 시 이미 존재하는 loginId 나 alias 인 경우, 예외 코드를 담은 DTO 와 HTTP 400 상태코드를 반환한다.")
    void joinFailedByAliasDuplication(final CreateUserRequest duplicated) throws Exception {
        //given
        when(userService.join(any())).thenThrow(new IllegalStateException("이미 사용중인 loginId 혹은 alias 입니다."));

        //when
        ResultActions actual = mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicated)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("이미 사용중인 loginId 혹은 alias 입니다."));
    }

    static Stream<Arguments> createUserRequestParameterProvider() {
        return Stream.of(
                of(new CreateUserRequest("apple123", "apple123!!", "아이시스")),
                of(new CreateUserRequest("banana123", "apple123!!", "삼다수"))
        );
    }
}
