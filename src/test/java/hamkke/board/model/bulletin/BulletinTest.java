package hamkke.board.model.bulletin;

import hamkke.board.model.bulletin.vo.Content;
import hamkke.board.model.bulletin.vo.Title;
import hamkke.board.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BulletinTest {

    private User createUser() {
        final String loginId = "apple123";
        final String password = "apple123!!";
        final String alias = "삼다수";
        final LocalDateTime createdDateTime = LocalDateTime.of(2022, 11, 5, 20, 5, 11);
        return new User(loginId, password, alias, createdDateTime);
    }

    private Bulletin createBulletin(final User author) {
        final String title = "sample title";
        final String content = "sample content";
        final LocalDateTime createdDateTime = LocalDateTime.of(2022, 11, 6, 12, 20);
        return new Bulletin(title, content, author, createdDateTime);
    }

    @Test
    @DisplayName("게시물 제목, 내용, 작성자, 작성 일시를 반환한다.")
    void getValues() {
        //given
        User user = createUser();
        Bulletin bulletin = createBulletin(user);

        // when
        Title title = bulletin.getTitle();
        Content content = bulletin.getContent();
        User author = bulletin.getAuthor();
        LocalDateTime createdDateTime = bulletin.getCreatedDateTime();

        //then
        assertAll(
                () -> assertThat(title).isEqualTo(new Title("sample title")),
                () -> assertThat(content).isEqualTo(new Content("sample content")),
                () -> assertThat(author).isEqualTo(user),
                () -> assertThat(createdDateTime).isEqualTo(LocalDateTime.of(2022, 11, 6, 12, 20)),
                () -> assertThat(bulletin.getLastModifiedDateTime()).isEqualTo(LocalDateTime.of(2022, 11, 6, 12, 20))
        );
    }

    @Test
    @DisplayName("입력 받은 Author 의 게시물과 동일한지 비교한다. - 연관 관계 편의 메서드")
    void changeAuthor() {
        //given
        User user = createUser();
        Bulletin bulletin = createBulletin(user);

        //when
        boolean doesAuthorHasBulletin = bulletin.getAuthor()
                .getBulletins()
                .contains(bulletin);

        //then
        assertThat(doesAuthorHasBulletin).isTrue();
    }
}
