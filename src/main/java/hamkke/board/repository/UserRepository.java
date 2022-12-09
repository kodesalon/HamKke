package hamkke.board.repository;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.LoginId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(final LoginId loginId);
}
