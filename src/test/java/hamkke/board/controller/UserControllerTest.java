package hamkke.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hamkke.board.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("loginId, password, alias 를 json 으로 전달받아 회원 가입을 하고, 201 상태 코드를 반환한다.")
    void join() throws Exception {
        //given
        Map<String, String> input = new HashMap<>();
        input.put("loginId", "apple123");
        input.put("password", "apple123!");
        input.put("alias", "아이폰");

        //when
        ResultActions actual = mockMvc.perform(
                post("/api/user/join").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)));

        //then
        actual.andExpect(status().isCreated());
    }
}
