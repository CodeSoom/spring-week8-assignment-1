package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.error.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFindService implements UserFindInterface {

    private final UserRepository userRepository;

    public UserFindService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자를 조회한다.
     *
     * @param id 조회할 사용자의 식별자
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 식별자에 해당하는 사용자
     */
    @Transactional(readOnly = true)
    @Override
    public User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
