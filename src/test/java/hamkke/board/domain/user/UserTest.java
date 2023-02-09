package hamkke.board.domain.user;

import hamkke.board.domain.bulletin.Bulletin;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.domain.user.vo.Password;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("apple123", "apple123!!", "삼다수");
    }

    @Test
    @DisplayName("유저 아이디, 비밀번호, 별칭, 생성 일자를 반환한다.")
    void getValues() {
        //given
        SoftAssertions softly = new SoftAssertions();

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
        Bulletin bulletin = createBulletin(user);

        //when
        user.addBulletin(bulletin);

        //then
        assertThat(user.getBulletins()).contains(bulletin);
    }

    private Bulletin createBulletin(final User author) {
        final String title = "sample title";
        final String content = "sample content";
        return new Bulletin(title, content, author);
    }

    @Test
    @DisplayName("Alias 를 변경한다.")
    void changeAlias() {
        //given
        String newAlias = "백산수";
        Alias expect = new Alias(newAlias);

        //when
        user.changeAlias(newAlias);

        //then
        assertThat(user.getAlias()).isEqualTo(expect);
    }

    @Test
    @DisplayName("password 를 변경한다.")
    void changePassword() {
        //given
        String newPassword = "banana123!!";
        Password expect = new Password(newPassword);

        //when
        user.changePassword(newPassword);

        //then
        assertThat(user.getPassword()).isEqualTo(expect);
    }
}
