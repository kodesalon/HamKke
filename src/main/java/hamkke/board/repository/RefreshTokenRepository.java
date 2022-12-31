package hamkke.board.repository;

import hamkke.board.web.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserLoginIdValue(String loginId);

    Optional<RefreshToken> findByToken(String token);
}
