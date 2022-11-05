package hamkke.board.model.user;

import hamkke.board.model.bulletin.Bulletin;
import hamkke.board.model.bulletin.vo.Content;
import hamkke.board.model.bulletin.vo.Title;
import hamkke.board.model.user.vo.Alias;
import hamkke.board.model.user.vo.Password;
import hamkke.board.model.user.vo.LoginId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserTest {

    @Test
    @DisplayName("유저 아이디, 비밀번호, 별칭, 생성 일자를 반환한다.")
    void getValues() {
        //given
        LoginId loginId = new LoginId("apple123");
        Password password = new Password("apple123!!");
        Alias alias = new Alias("삼다수");
        LocalDateTime createdDateTime = LocalDateTime.now();

        User user = new User(loginId, password, alias, createdDateTime);

        //when
        LoginId actualLoginId = user.getLoginId();
        Password actualPassword = user.getPassword();
        Alias actualAlias = user.getAlias();
        LocalDateTime actualCreatedTime = user.getCreatedDateTime();
        // then
        assertAll(
                () -> assertThat(actualLoginId).isEqualTo(loginId),
                () -> assertThat(actualPassword).isEqualTo(password),
                () -> assertThat(actualAlias).isEqualTo(alias),
                () -> assertThat(actualCreatedTime).isEqualTo(createdDateTime)
        );
    }

    @Test
    @DisplayName("게시물을 입력 받아 추가한다.")
    void addBulletin() {
        //given
        LoginId loginId = new LoginId("apple123");
        Password password = new Password("apple123!!");
        Alias alias = new Alias("삼다수");
        LocalDateTime createdDateTime = LocalDateTime.now();

        User user = new User(loginId, password, alias, createdDateTime);
        Bulletin bulletin = new Bulletin(new Title("제목"), new Content("본문 내용"), user, LocalDateTime.now());

        //when
        user.addBulletin(bulletin);
        boolean hasBulletin = user.getBulletins()
                .contains(bulletin);

        //then
        assertThat(hasBulletin).isTrue();
    }
}
