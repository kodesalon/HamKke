package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.Alias;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(final CreateUserRequest createUserRequest) {
        validateDuplication(createUserRequest);
        User user = new User(createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias());
        User savedMember = userRepository.save(user);
        return savedMember.getId();
    }
    private void validateDuplication(final CreateUserRequest createUserRequest) {
        validateLoginIdDuplication(createUserRequest);
        validateAliasDuplication(createUserRequest);
    }
    private void validateLoginIdDuplication(final CreateUserRequest createUserRequest) {
        String loginId = createUserRequest.getLoginId();
        userRepository.findUserByLoginId(new LoginId(loginId))
                .ifPresent(user -> {
                    log.debug("중복된 아이디 생성 loginId = {}", createUserRequest.getLoginId());
                    throw new IllegalStateException("해당 ID는 이미 사용중 입니다.");
                });
    }

    private void validateAliasDuplication(final CreateUserRequest createUserRequest) {
        String alias = createUserRequest.getAlias();
        userRepository.findUserByAlias(new Alias(alias))
                .ifPresent(user -> {
                    log.debug("중복된 별명 생성 loginId = {}", createUserRequest.getLoginId());
                    throw new IllegalStateException("해당 별명은 이미 사용중 입니다.");
                });
    }
}
