package hamkke.board.model.bulletin;

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

    public Bulletin(final Title title, final Content content, final User author, final LocalDateTime createdDateTime) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = createdDateTime;
    }

    public void changeUser(final User user) {
        this.author = user;
        user.getBulletins().add(this);
    }
}
