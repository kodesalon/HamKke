package hamkke.board.domain.user;

import hamkke.board.domain.bulletin.Bulletin;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.domain.user.vo.Password;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User createUser() {
        final String loginId = "apple123";
        final String password = "apple123!!";
        final String alias = "삼다수";
        return new User(loginId, password, alias);
    }

    private Bulletin createBulletin(final User author) {
        final String title = "sample title";
        final String content = "sample content";
        return new Bulletin(title, content, author);
    }

    @Test
    @DisplayName("유저 아이디, 비밀번호, 별칭, 생성 일자를 반환한다.")
    void getValues() {
        //given
        SoftAssertions softly = new SoftAssertions();
        User user = createUser();

        //when, then
        softly.assertThat(user.getLoginId()).isEqualTo(new LoginId("apple123"));
        softly.assertThat(user.getPassword()).isEqualTo(new Password("apple123!!"));
        softly.assertThat(user.getAlias()).isEqualTo(new Alias("삼다수"));
        softly.assertAll();
    }

    @Test
    @DisplayName("연관 관계 편의 메서드")
    void addBulletin() {
        //given
        User user = createUser();
        Bulletin bulletin = createBulletin(user);

        //when
        user.addBulletin(bulletin);

        //then
        assertThat(user.getBulletins()).contains(bulletin);
    }

    @Test
    @DisplayName("Alias 를 변경한다.")
    void changeAlias() {
        //given
        User user = createUser();
        String newAlias = "백산수";

        //when
        user.changeAlias(newAlias);

        //then
        assertThat(user.getAlias().getValue()).isEqualTo("백산수");
    }

    @Test
    @DisplayName("password 를 변경한다.")
    void changePassword() {
        //given
        User user = createUser();
        String newPassword = "banana123!!";
        Password expect = new Password(newPassword);

        //when
        user.changePassword(newPassword);

        //then
        assertThat(user.getPassword()).isEqualTo(expect);
    }
}
