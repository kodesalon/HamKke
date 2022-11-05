package hamkke.board.model.user;

import hamkke.board.model.bulletin.Bulletin;
import hamkke.board.model.user.vo.Alias;
import hamkke.board.model.user.vo.Password;
import hamkke.board.model.user.vo.LoginId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private LoginId loginId;

    @Embedded
    private Password password;

    @Embedded
    private Alias alias;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private final List<Bulletin> bulletins = new ArrayList<>();

    private LocalDateTime createdDateTime;

    public User(final String loginId, final String password, final String alias, final LocalDateTime createdDateTime) {
        this.loginId = new LoginId(loginId);
        this.password = new Password(password);
        this.alias = new Alias(alias);
        this.createdDateTime = createdDateTime;
    }

    public void addBulletin(final Bulletin bulletin) {
        this.bulletins
                .add(bulletin);
    }
}
