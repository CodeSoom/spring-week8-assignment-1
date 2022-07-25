package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 회원 생성, 수정, 삭제 서비스
 */
@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(Mapper dozerMapper,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원을 생성하고, 생성된 회원을 반환한다.
     *
     * @param registrationData 생성할 회원의 정보
     * @return 생성된 회원
     */
    public User registerUser(UserRegistrationData registrationData) {
        String email = registrationData.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }

        User user = userRepository.save(
                mapper.map(registrationData, User.class));

        user.changePassword(registrationData.getPassword(), passwordEncoder);

        roleRepository.save(new Role(user.getId(), "USER"));

        return user;
    }

    /**
     * id로 조회된 회원의 정보를 수정하고, 수정된 회원을 반환한다.
     *
     * @param id 회원의 id
     * @param modificationData 수정할 정보
     * @param userId 회원의 id
     * @return 수정된 회원
     * @throws AccessDeniedException 인증에 실패할 경우 발생
     * @throws UserNotFoundException id와 일치하는 회원이 조회되지 않는 경우에 발생
     */
    public User updateUser(Long id, UserModificationData modificationData,
                           Long userId) throws AccessDeniedException {
        if (!id.equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * id로 조회된 회원을 삭제하고, 삭제된 회원을 반환한다.
     * 
     * @param id 회원의 id
     * @return 삭제된 회원
     * @throws UserNotFoundException id와 일치하는 회원이 조회되지 않는 경우에 발생
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * id로 회원을 찾고, 찾은 회원을 반환한다.
     *
     * @param id 회원의 id
     * @return 조회된 회원
     * @throws UserNotFoundException id와 일치하는 회원이 조회되지 않은 경우에 발생
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
