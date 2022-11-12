package hamkke.board.domain.user;

import hamkke.board.domain.base.BaseEntity;
import hamkke.board.domain.bulletin.Bulletin;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.domain.user.vo.Password;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

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

    public User(final String loginId, final String password, final String alias) {
        this.loginId = new LoginId(loginId);
        this.password = new Password(password);
        this.alias = new Alias(alias);
    }

    public void addBulletin(final Bulletin bulletin) {
        this.bulletins
                .add(bulletin);
    }
}
