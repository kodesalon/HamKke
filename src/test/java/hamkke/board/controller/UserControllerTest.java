package hamkke.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamkke.board.service.UserService;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

    private CreateUserRequest generateUserRequest() {
        return new CreateUserRequest("apple123", "apple123!!", "아이폰");
    }

    @Test
    @DisplayName("loginId, password, alias 를 json 으로 전달받아 회원 가입을 하고, HTTP 201 상태 코드를 반환한다.")
    void join() throws Exception {
        //given
        given(userService.join(any())).willReturn(1L);
        CreateUserRequest createUserRequest = generateUserRequest();

        //when
        ResultActions actual = mockMvc.perform(
                post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)));

        //then
        actual.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("1"));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 loginId 인 경우, 예외 코드를 담은 DTO 와 HTTP 400 상태코드를 반환한다.")
    void joinFailedByLoginIdDuplication() throws Exception {
        //given
        given(userService.join(any())).willReturn(1L)
                .willThrow(new IllegalStateException("해당 ID는 이미 사용중 입니다."));

        CreateUserRequest existedUserRequest = generateUserRequest();
        CreateUserRequest duplicationUserRequest = new CreateUserRequest("apple123", "banana123!", "마우스");
        mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existedUserRequest)));

        //when
        ResultActions actual = mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicationUserRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("해당 ID는 이미 사용중 입니다."));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 alias 인 경우, 예외 코드를 담은 DTO 와 HTTP 400 상태코드를 반환한다.")
    void joinFailedByAliasDuplication() throws Exception {
        //given
        given(userService.join(any())).willReturn(1L)
                .willThrow(new IllegalStateException("해당 별명은 이미 사용중 입니다."));

        CreateUserRequest existedUserRequest = generateUserRequest();
        CreateUserRequest duplicationUserRequest = new CreateUserRequest("apple123", "apple123!!", "마우스");
        mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existedUserRequest)));

        //when
        ResultActions actual = mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicationUserRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("해당 별명은 이미 사용중 입니다."));
    }
}
