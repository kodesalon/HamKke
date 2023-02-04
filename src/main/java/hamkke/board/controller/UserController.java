package hamkke.board.controller;

import hamkke.board.service.dto.user.UserService;
import hamkke.board.service.dto.user.request.CreateUserRequest;
import hamkke.board.service.dto.user.request.UserChangeAliasRequest;
import hamkke.board.service.dto.user.request.UserChangePasswordRequest;
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
    public ResponseEntity<Void> changeAlias(@Login final String loginId, @Validated @RequestBody final UserChangeAliasRequest userChangeAliasRequest) {
        log.info("loginId = {} 의 alias 변경 요청 newAlias = {}", loginId, userChangeAliasRequest.getNewAlias());
        userService.changeAlias(loginId, userChangeAliasRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Login final String loginId, @Validated @RequestBody final UserChangePasswordRequest userChangePasswordRequest) {
        log.info("loginId = {} 의 password 변경 요청 newPassword = {}", loginId, userChangePasswordRequest.getNewPassword());
        userService.changePassword(loginId, userChangePasswordRequest);
        return ResponseEntity.ok()
                .build();
    }
}
