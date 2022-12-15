package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.domain.user.vo.LoginId;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import hamkke.board.service.dto.LoginRequest;
import hamkke.board.service.dto.LoginServiceResponseDto;
import hamkke.board.web.jwt.TokenResolver;
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
    private final TokenResolver tokenResolver;

    @Transactional
    public Long join(final CreateUserRequest createUserRequest) {
        User savedUser = saveUser(createUserRequest);
        return savedUser.getId();
    }

    private User saveUser(final CreateUserRequest createUserRequest) {
        try {
            return userRepository.save(new User(createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias()));
        } catch (final DataIntegrityViolationException exception) {
            UniqueConstraintCondition uniqueConstraintCondition = UniqueConstraintCondition.matchCondition(exception);
            throw uniqueConstraintCondition.generateException();
        }
    }

    public LoginServiceResponseDto login(final LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        User user = userRepository.findByLoginId(new LoginId(loginId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
        user.checkPassword(loginRequest.getPassword());
        String token = tokenResolver.createToken(user.getId());
        return new LoginServiceResponseDto(token, user.getId(), user.getAlias().getValue());
    }
}
