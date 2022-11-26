package hamkke.board.domain.bulletin;

import hamkke.board.domain.bulletin.vo.Content;
import hamkke.board.domain.bulletin.vo.Title;
import hamkke.board.domain.user.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BulletinTest {

    @PersistenceContext
    private EntityManager em;

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
    @DisplayName("게시물 제목, 내용, 작성자, 작성 일시를 반환한다.")
    void getValues() {
        //given
        SoftAssertions softly = new SoftAssertions();
        User user = createUser();
        Bulletin bulletin = createBulletin(user);

        // when
        Title title = bulletin.getTitle();
        Content content = bulletin.getContent();
        User author = bulletin.getAuthor();

        //then
        softly.assertThat(title).isEqualTo(new Title("sample title"));
        softly.assertThat(content).isEqualTo(new Content("sample content"));
        softly.assertThat(author).isEqualTo(user);
        softly.assertAll();
    }

    @Test
    @DisplayName("연관 관계 편의 메서드")
    void changeAuthor() {
        //given
        User user = createUser();

        //when
        Bulletin bulletin = createBulletin(user);

        //then
        assertThat(bulletin.getAuthor().getBulletins()).contains(bulletin);
    }

    @Test
    @DisplayName("해당 게시물이 입력받은 user 가 작성한 게시물인지 확인한다. ")
    void isSameAuthor() {
        //given
        User user = createUser();
        Bulletin bulletin = new Bulletin("sample title", "sample content", user);
        em.persist(user);
        em.persist(bulletin);
        em.flush();
        em.clear();

        //when
        boolean actual = bulletin.isSameAuthor(user);

        //then
        assertThat(actual).isTrue();
    }
}
