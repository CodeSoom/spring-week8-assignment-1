package com.codesoom.assignment.application.users;

import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 회원 변경을 담당합니다.
 */
@Transactional
@Service
public class UserCommandService implements UserSaveService, UserUpdateService, UserDeleteService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(UserSaveRequest userSaveRequest) {
        User user = userSaveRequest.user();
        user.changePassword(userSaveRequest.getPassword(), passwordEncoder);
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, UserSaveRequest userSaveRequest) {
        User user = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("%s에 해당하는 회원을 찾을 수 없어 수정에 실패하였습니다.", id)));

        user.update(userSaveRequest.user());
        user.changePassword(userSaveRequest.getPassword(), passwordEncoder);

        return user;
    }

    @Override
    public User deleteUser(Long id) {
        User user = repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("%s에 해당하는 회원을 찾을 수 없어 삭제에 실패하였습니다.", id)));

        user.destroy();
        return user;
    }

}
