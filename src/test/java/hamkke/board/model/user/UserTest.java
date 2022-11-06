package hamkke.board.model.user;

import hamkke.board.model.bulletin.Bulletin;
import hamkke.board.model.user.vo.Alias;
import hamkke.board.model.user.vo.LoginId;
import hamkke.board.model.user.vo.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        final String loginId = "apple123";
        final String password = "apple123!!";
        final String alias = "삼다수";
        final LocalDateTime createdDateTime = LocalDateTime.of(2022, 11, 5, 20, 5, 11);

        user = new User(loginId, password, alias, createdDateTime);
    }

    @Test
    @DisplayName("유저 아이디, 비밀번호, 별칭, 생성 일자를 반환한다.")
    void getValues() {
        //when
        LoginId actualLoginId = user.getLoginId();
        Password actualPassword = user.getPassword();
        Alias actualAlias = user.getAlias();
        LocalDateTime actualCreatedTime = user.getCreatedDateTime();
        // then
        assertAll(
                () -> assertThat(actualLoginId).isEqualTo(new LoginId("apple123")),
                () -> assertThat(actualPassword).isEqualTo(new Password("apple123!!")),
                () -> assertThat(actualAlias).isEqualTo(new Alias("삼다수")),
                () -> assertThat(actualCreatedTime).isEqualTo(LocalDateTime.of(2022, 11, 5, 20, 5, 11))
        );
    }

    @Test
    @DisplayName("게시물을 입력 받아 추가한다.")
    void addBulletin() {
        //given
        Bulletin bulletin = new Bulletin("제목", "본문 내용", user, LocalDateTime.now());

        //when
        user.addBulletin(bulletin);
        boolean hasBulletin = user.getBulletins()
                .contains(bulletin);

        //then
        assertThat(hasBulletin).isTrue();
    }
}
