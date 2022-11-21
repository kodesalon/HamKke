package hamkke.board.controller;

import hamkke.board.service.UserService;
import hamkke.board.service.dto.CreateUserRequest;
import hamkke.board.service.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponse> join(@Validated @RequestBody final CreateUserRequest createUserRequest) {
        log.info("join 요청 loginId = {}, password = {}, alias = {}", createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias());
        Long joinedUserId = userService.join(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(joinedUserId));
    }
}

