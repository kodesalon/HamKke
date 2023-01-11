package hamkke.board.domain.user;

import hamkke.board.domain.bulletin.Bulletin;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.domain.user.vo.Password;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private User createUser() {
        final String loginId = "apple123";
        final String password = "apple123!!";
        final String alias = "삼다수";
        return new User(loginId, password, alias);
    }

    @Test
    @DisplayName("유저 아이디, 비밀번호, 별칭, 생성 일자를 반환한다.")
    void getValues() {
        //given
        SoftAssertions softly = new SoftAssertions();
        User user = createUser();

        //when
        LoginId actualLoginId = user.getLoginId();
        Password actualPassword = user.getPassword();
        Alias actualAlias = user.getAlias();

        // then
        softly.assertThat(actualLoginId).isEqualTo(new LoginId("apple123"));
        softly.assertThat(actualPassword).isEqualTo(new Password("apple123!!"));
        softly.assertThat(actualAlias).isEqualTo(new Alias("삼다수"));
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물을 입력 받아 추가한다.")
    void addBulletin() {
        //given
        User user = createUser();
        Bulletin bulletin = new Bulletin("제목", "본문 내용", user);

        //when
        user.addBulletin(bulletin);
        boolean hasBulletin = user.getBulletins()
                .contains(bulletin);

        //then
        assertThat(hasBulletin).isTrue();
    }

    @Test
    @DisplayName("입력받은 비밀번호가 일치하지 않으면 예외를 반환한다.")
    void checkPassword() {
        //given
        User user = createUser();
        String otherPassword = "banana123";

        //when, then
        assertThatThrownBy(() -> user.checkPassword(otherPassword)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
