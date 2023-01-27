package hamkke.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserChangeAliasRequest {

    @NotNull
    @Pattern(regexp = "^[a-z0-9가-힣]{2,8}$", message = "별칭은 특수문자와 영어(대문자)를 제외하여 1자 이상, 8자 이하여야 합니다.")
    private String newAlias;

    public UserChangeAliasRequest(final String newAlias) {
        this.newAlias = newAlias;
    }
}
