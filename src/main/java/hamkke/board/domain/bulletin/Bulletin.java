package hamkke.board.domain.bulletin;

import hamkke.board.domain.base.BaseEntity;
import hamkke.board.domain.bulletin.vo.Content;
import hamkke.board.domain.bulletin.vo.Title;
import hamkke.board.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bulletin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_id")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    public Bulletin(final String title, final String content, final User author) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.author = author;
        this.author.addBulletin(this);
    }

    public boolean isSameAuthor(final User author) {
        return this.author
                .equals(author);
    }
}
