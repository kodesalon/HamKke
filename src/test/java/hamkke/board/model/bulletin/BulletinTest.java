package hamkke.board.model.bulletin;

import hamkke.board.model.bulletin.vo.Content;
import hamkke.board.model.bulletin.vo.Title;
import hamkke.board.model.user.User;
import hamkke.board.model.user.vo.Alias;
import hamkke.board.model.user.vo.Password;
import hamkke.board.model.user.vo.UserLoginId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BulletinTest {

    private final Title title = new Title("sample title");
    private final Content content = new Content("sample content");
    private final User author = new User(new UserLoginId("apple123"), new Password("apple12341"), new Alias("별명"), LocalDateTime.now());
    private final LocalDateTime createdDateTime = LocalDateTime.now();

    @Test
    @DisplayName("게시물 제목, 내용, 작성자, 작성 일시를 반환한다.")
    void getValues() {
        //given
        Bulletin bulletin = new Bulletin(title, content, author, createdDateTime);

        //then
        assertAll(
                () -> assertThat(bulletin.getTitle()).isEqualTo(title),
                () -> assertThat(bulletin.getContent()).isEqualTo(content),
                () -> assertThat(bulletin.getAuthor()).isEqualTo(author),
                () -> assertThat(bulletin.getCreatedDateTime()).isEqualTo(createdDateTime),
                () -> assertThat(bulletin.getLastModifiedDateTime()).isEqualTo(createdDateTime)
        );
    }

    @Test
    @DisplayName("입력 받은 Author 의 게시물과 동일한지 비교한다. - 연관 관계 편의 메서드")
    void changeAuthor() {
        Bulletin bulletin = new Bulletin(title, content, author, createdDateTime);
        boolean hasAuthor = bulletin.getAuthor()
                .getBulletins()
                .contains(bulletin);
        assertThat(hasAuthor).isTrue();
    }
}
