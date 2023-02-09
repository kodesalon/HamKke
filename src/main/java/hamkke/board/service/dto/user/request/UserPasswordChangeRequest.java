package hamkke.board.service.dto.user.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPasswordChangeRequest {

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*_-])[A-Za-z\\d!@#$%^&*_-]{8,18}$",
            message = "비밀번호는 영문(대문자,소문자), 특수문자(!,@,#,$,%,^,&,*,_,-), 숫자를 최소 1개 이상 조합한 8자 이상 18자 이하여야합니다.")
    private String newPassword;

    public UserPasswordChangeRequest(final String newPassword) {
        this.newPassword = newPassword;
    }
}
