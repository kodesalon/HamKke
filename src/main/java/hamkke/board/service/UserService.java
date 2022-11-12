package hamkke.board.service;

import hamkke.board.domain.user.User;
import hamkke.board.repository.UserRepository;
import hamkke.board.service.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(final CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getLoginId(), createUserRequest.getPassword(), createUserRequest.getAlias());
        User savedMember = userRepository.save(user);
        return savedMember.getId();
    }
}
