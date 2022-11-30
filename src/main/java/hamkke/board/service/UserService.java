package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private static final String ALIAS_UNIQUE_CONSTRAINT_CONDITION = "alias_unique";
    private static final String LOGIN_ID_UNIQUE_CONSTRAINT_CONDITION = "login_id_unique";

    private final UserRepository userRepository;

    @Transactional
    public Long join(final CreateUserRequest createUserRequest) {
        User savedUser = saveUser(createUserRequest);
        return savedUser.getId();
    }

    private User saveUser(final CreateUserRequest createUserRequest) {
        try {
            return userRepository.save(new User(createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias()));
        } catch (final DataIntegrityViolationException exception) {
            String message = exception.getMessage();
            if (hasUniqueConstraintCondition(message, LOGIN_ID_UNIQUE_CONSTRAINT_CONDITION)) {
                throw new IllegalStateException("이미 존재하는 ID 입니다.");
            }
            if (hasUniqueConstraintCondition(message, ALIAS_UNIQUE_CONSTRAINT_CONDITION)) {
                throw new IllegalStateException("이미 존재하는 별명 입니다.");
            }
        }
        throw new IllegalStateException("잘못된 접근입니다.");
    }

    private static boolean hasUniqueConstraintCondition(final String message, final String uniqueConstraintCondition) {
        return message != null && message.contains(uniqueConstraintCondition);
    }
}
