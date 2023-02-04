package hamkke.board.service.dto.authentication.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class LoginRequest {

    @NotNull
    @Pattern(regexp = "^[a-z]+[a-z 0-9]{5,19}$", message = "유저의 아이디는 공백을 제외하고, 영문자(소문자)로 시작한 영문자(소문자)와 숫자의 조합으로 6자 이상 20자 이하여야합니다.")
    private final String loginId;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*_-])[A-Za-z\\d!@#$%^&*_-]{8,18}$", message = "비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야합니다.")
    private final String password;

    public LoginRequest(final String loginId, final String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
