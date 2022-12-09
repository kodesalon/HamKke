package hamkke.board.repository;

import hamkke.board.config.JpaAuditingConfiguration;
import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(JpaAuditingConfiguration.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User("apple123", "apple123!!", "삼다수");
        userRepository.save(user);
    }

    @Test
    @DisplayName("loginId 를 입력 받아, 해당하는 loginId를 가진 User 를 반환한다.")
    void findByLoginId() {
        //given
        LoginId loginId = new LoginId("apple123");
        SoftAssertions softly = new SoftAssertions();

        //when
        User findUser = userRepository.findByLoginId(loginId).get();

        //then
        softly.assertThat(findUser.getLoginId()).isEqualTo(new LoginId("apple123"));
        softly.assertThat(findUser.getAlias()).isEqualTo(new Alias("삼다수"));
        softly.assertAll();
    }

    @Test
    @DisplayName("loginId 를 입력 받아, 해당하는 loginId가 DB에 없으면 Optional.empty 를 반환한다.")
    void findUserByLoginIdWhenEmpty() {
        //given
        LoginId notExistingLoginId = new LoginId("banana123");

        //when
        Optional<User> userByLoginId = userRepository.findByLoginId(notExistingLoginId);

        //then
        assertThat(userByLoginId).isEmpty();
    }
}
