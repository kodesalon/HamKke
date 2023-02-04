package hamkke.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamkke.board.service.UserService;
import hamkke.board.service.dto.CreateUserRequest;
import hamkke.board.service.dto.UserChangeAliasRequest;
import hamkke.board.web.jwt.TokenResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenResolver tokenResolver;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("loginId, password, alias 를 json 으로 전달받아 회원 가입을 하고, HTTP 201 상태 코드를 반환한다.")
    void join() throws Exception {
        //given
        when(userService.join(any(CreateUserRequest.class))).thenReturn("apple123");

        CreateUserRequest createUserRequest = new CreateUserRequest("apple123", "apple123!!", "아이폰");

        //when
        ResultActions actual = mockMvc.perform(
                post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)));

        //then
        actual.andExpect(status().isCreated())
                .andExpect(jsonPath("$.loginId").value("apple123"));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 loginId 인 경우, 예외 코드를 담은 DTO 와 HTTP 400 상태코드를 반환한다.")
    void joinFailedByLoginIdDuplication() throws Exception {
        //given
        when(userService.join(any())).thenThrow(new IllegalStateException("이미 존재하는 ID 입니다."));

        CreateUserRequest duplicated = new CreateUserRequest("apple123", "apple123!!", "아이시스");

        //when
        ResultActions actual = mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicated)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("이미 존재하는 ID 입니다."));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 별명인 경우, 예외 코드를 담은 DTO 와 HTTP 400 상태코드를 반환한다.")
    void joinFailedByAliasDuplication() throws Exception {
        //given
        when(userService.join(any())).thenThrow(new IllegalStateException("이미 존재하는 별명 입니다."));

        CreateUserRequest duplicated = new CreateUserRequest("banana123", "apple123!!", "아이시스");

        //when
        ResultActions actual = mockMvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicated)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("이미 존재하는 별명 입니다."));
    }

    @Test
    @DisplayName("별명 변경 시, 새로운 별명을 입력받고 변경 후, HTTP 200 상태코드를 반환한다.")
    void changeAlias() throws Exception {
        //given
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("새로운별명");

        //when
        ResultActions actual = mockMvc.perform(put("/api/user/alias").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userChangeAliasRequest)));

        //then
        actual.andExpect(status().isOk());
    }

    @Test
    @DisplayName("별명 변경 시, 새로운 별명을 입력받고 변경 중 별명의 제약조건이 일치하지 않는다면, HTTP 400 상태코드를 반환한다.")
    void changeAliasFailed() throws Exception {
        //given
        when(tokenResolver.getLoginId(anyString())).thenReturn("apple123");
        doThrow(new IllegalArgumentException("별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상, 8자 이하여야 합니다.")).when(userService)
                .changeAlias(anyString(), any(UserChangeAliasRequest.class));

        String inputLoginId = "apple123";
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("사용못하는별명");

        //when
        ResultActions actual = mockMvc.perform(put("/api/user/alias").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", inputLoginId)
                .content(objectMapper.writeValueAsString(userChangeAliasRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상, 8자 이하여야 합니다."));
    }

    @Test
    @DisplayName("별명 변경 시, 새로운 별명이 이미 존재한다면, HTTP 400 상태코드를 반환한다.")
    void changeAliasFailedByDuplicationAlias() throws Exception {
        //given
        when(tokenResolver.getLoginId(anyString())).thenReturn("apple123");
        doThrow(new IllegalArgumentException("이미 존재하는 별명입니다.")).when(userService)
                .changeAlias(anyString(), any(UserChangeAliasRequest.class));

        String inputLoginId = "apple123";
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("이미존재하는별명");

        //when
        ResultActions actual = mockMvc.perform(put("/api/user/alias").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", inputLoginId)
                .content(objectMapper.writeValueAsString(userChangeAliasRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("이미 존재하는 별명입니다."));
    }

    @Test
    @DisplayName("별명 변경 시, 새로운 별명을 입력받고 변경 중 로그인 아이디가 존재하지 않는다면, HTTP 400 상태코드를 반환한다.")
    void changeAliasFailedByIncorrectLoginId() throws Exception {
        //given
        when(tokenResolver.getLoginId(anyString())).thenReturn("apple123");
        doThrow(new IllegalArgumentException("존재하지 않는 ID 입니다.")).when(userService)
                .changeAlias(anyString(), any(UserChangeAliasRequest.class));

        String inputLoginId = "apple123";
        UserChangeAliasRequest userChangeAliasRequest = new UserChangeAliasRequest("사용못하는별명");

        //when
        ResultActions actual = mockMvc.perform(put("/api/user/alias").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", inputLoginId)
                .content(objectMapper.writeValueAsString(userChangeAliasRequest)));

        //then
        actual.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("존재하지 않는 ID 입니다."));
    }
}
