package hamkke.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamkke.board.service.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardIntegratedTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequest generateCreateUserRequest() {
        return new CreateUserRequest("apple123", "apple123!!", "삼다수");
    }

    @Test
    @DisplayName("회원 가입 요청 시, 회원 가입 후 Http 201 상태코드를 반환한다.")
    void join() throws Exception {
        //given
        CreateUserRequest createUserRequest = generateCreateUserRequest();

        //when
        ResultActions actual = mvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        //then
        actual.andExpect(status().isCreated());
        actual.andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @DisplayName("회원 가입 요청 시, loginId 가 이미 존재한다면 Http 400 상태코드를 반환한다.")
    void joinFailedByLoginIdDuplication() throws Exception {
        //given
        CreateUserRequest createUserRequest = generateCreateUserRequest();
        CreateUserRequest duplicatedLoginIdRequest = new CreateUserRequest("apple123", "banana123!!", "아이시스");
        mvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        //when
        ResultActions actual = mvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicatedLoginIdRequest)));

        //then
        actual.andExpect(status().isBadRequest());
        actual.andExpect(jsonPath("$.error").value("해당 ID는 이미 사용중 입니다."));
    }

    @Test
    @DisplayName("회원 가입 요청 시, loginId 가 이미 존재한다면 Http 400 상태코드를 반환한다.")
    void joinFailedByAliasDuplication() throws Exception {
        //given
        CreateUserRequest createUserRequest = new CreateUserRequest("apple123", "apple123!!", "삼다수");
        CreateUserRequest duplicatedLoginIdRequest = new CreateUserRequest("banana123", "banana123!!", "삼다수");
        mvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)));

        //when
        ResultActions actual = mvc.perform(post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicatedLoginIdRequest)));

        //then
        actual.andExpect(status().isBadRequest());
        actual.andExpect(jsonPath("$.error").value("해당 별명은 이미 사용중 입니다."));
    }
}
