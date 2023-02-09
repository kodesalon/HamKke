package hamkke.board.domain.user;

import hamkke.board.domain.base.BaseEntity;
import hamkke.board.domain.bulletin.Bulletin;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.domain.user.vo.Password;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "login_id_unique",
                        columnNames = {"login_id"}
                ),
                @UniqueConstraint(
                        name = "alias_unique",
                        columnNames = {"alias"}
                )
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void checkPassword(final String otherPassword) {
        password.checkPassword(otherPassword);
    }

    public void changePassword(final String newPassword) {
        password.changePassword(newPassword);
    }
    public void changeAlias(final String newAlias) {
        alias.changeValue(newAlias);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
