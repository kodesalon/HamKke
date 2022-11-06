package hamkke.board.model.bulletin;

import hamkke.board.model.bulletin.vo.Content;
import hamkke.board.model.bulletin.vo.Title;
import hamkke.board.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bulletin {

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

    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;

    public Bulletin(final String title, final String content, final User author, final LocalDateTime createdDateTime) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.author = author;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = createdDateTime;
        this.author.addBulletin(this);
    }
}