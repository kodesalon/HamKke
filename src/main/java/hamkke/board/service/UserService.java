package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.user.request.CreateUserRequest;
import hamkke.board.service.dto.user.request.UserAliasChangeRequest;
import hamkke.board.service.dto.user.request.UserPasswordChangeRequest;
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

    private final UserRepository userRepository;

    @Transactional
    public String join(final CreateUserRequest createUserRequest) {
        User savedUser = saveUser(createUserRequest);
        return savedUser.getLoginId()
                .getValue();
    }

    private User saveUser(final CreateUserRequest createUserRequest) {
        try {
            return userRepository.save(new User(createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias()));
        } catch (final DataIntegrityViolationException exception) {
            UniqueConstraintCondition uniqueConstraintCondition = UniqueConstraintCondition.matchCondition(exception);
            throw uniqueConstraintCondition.generateException();
        }
    }

    public User findByLoginId(final String loginId) {
        return userRepository.findByLoginIdValue(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
    }

    @Transactional
    public void changeAlias(final String loginId, final UserAliasChangeRequest userAliasChangeRequest) {
        User user = findByLoginId(loginId);

        try {
            user.changeAlias(userAliasChangeRequest.getNewAlias());
        } catch (final DataIntegrityViolationException exception) {
            UniqueConstraintCondition uniqueConstraintCondition = UniqueConstraintCondition.matchCondition(exception);
            throw uniqueConstraintCondition.generateException();
        }
    }

    @Transactional
    public void changePassword(final String loginId, final UserPasswordChangeRequest userPasswordChangeRequest) {
        User user = findByLoginId(loginId);
        user.changePassword(userPasswordChangeRequest.getNewPassword());
    }

    @Transactional
    public void withdraw(final String loginId) {
        User user = findByLoginId(loginId);
        userRepository.delete(user);
    }
}
