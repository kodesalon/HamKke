package hamkke.board.model.bulletin;

import hamkke.board.model.bulletin.vo.Content;
import hamkke.board.model.bulletin.vo.Title;
import hamkke.board.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BulletinTest {

    private Bulletin bulletin;
    private User author;

    @BeforeEach
    void setUp() {
        final String title = "sample title";
        final String content = "sample content";
        final LocalDateTime createdDateTime = LocalDateTime.of(2022, 11, 6, 12, 20);

        author = new User("apple123", "apple12341", "별명", LocalDateTime.of(2022, 11, 6, 12, 20));
        bulletin = new Bulletin(title, content, author, createdDateTime);
    }


    @Test
    @DisplayName("게시물 제목, 내용, 작성자, 작성 일시를 반환한다.")
    void getValues() {

        //then
        assertAll(
                () -> assertThat(bulletin.getTitle()).isEqualTo(new Title("sample title")),
                () -> assertThat(bulletin.getContent()).isEqualTo(new Content("sample content")),
                () -> assertThat(bulletin.getAuthor()).isEqualTo(author),
                () -> assertThat(bulletin.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2022, 11, 6, 12, 20)),
                () -> assertThat(bulletin.getLastModifiedDateTime()).isEqualTo(LocalDateTime.of(2022, 11, 6, 12, 20))
        );
    }

    @Test
    @DisplayName("입력 받은 Author 의 게시물과 동일한지 비교한다. - 연관 관계 편의 메서드")
    void changeAuthor() {
        //when
        boolean hasAuthor = bulletin.getAuthor()
                .getBulletins()
                .contains(bulletin);

        //then
        assertThat(hasAuthor).isTrue();
    }
}
