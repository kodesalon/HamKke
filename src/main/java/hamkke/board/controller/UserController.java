package hamkke.board.controller;

import hamkke.board.service.UserService;
import hamkke.board.service.dto.user.request.CreateUserRequest;
import hamkke.board.service.dto.user.request.UserAliasChangeRequest;
import hamkke.board.service.dto.user.request.UserPasswordChangeRequest;
import hamkke.board.service.dto.user.response.UserResponse;
import hamkke.board.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserResponse> join(@Validated @RequestBody final CreateUserRequest createUserRequest) {
        log.info("join 요청 loginId = {}, password = {}, alias = {}", createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias());
        String joinedUserLoginId = userService.join(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(joinedUserLoginId));
    }

    @PutMapping("/alias")
    public ResponseEntity<Void> changeAlias(@Login final String loginId, @Validated @RequestBody final UserAliasChangeRequest userAliasChangeRequest) {
        log.info("loginId = {} 의 alias 변경 요청 newAlias = {}", loginId, userAliasChangeRequest.getNewAlias());
        userService.changeAlias(loginId, userAliasChangeRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Login final String loginId, @Validated @RequestBody final UserPasswordChangeRequest userPasswordChangeRequest) {
        log.info("loginId = {} 의 password 변경 요청", loginId);
        userService.changePassword(loginId, userPasswordChangeRequest);
        return ResponseEntity.ok()
                .build();
    }
}
