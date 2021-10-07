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
 * 사용자의 생성, 조회, 수정, 삭제를 담당한다.
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
     * 생성한 사용자를 리턴한다.
     *
     * @param registrationData 사용자 생성에 필요한 데이터
     * @return 생성한 사용자
     * @throws UserEmailDuplicationException 생성할 사용자의 이메일이 이미 존재하는 경우
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
     * 수정한 사용자 정보를 리턴한다.
     *
     * @param id 수정할 사용자의 id
     * @param modificationData 수정할 사용자 데이터
     * @param userId 수정을 요청한 사용자의 id
     * @return 수정한 사용자
     * @throws UserNotFoundException 수정할 사용자를 찾지 못한 경우
     * @throws AccessDeniedException 수정할 사용자와 수정을 요청한 사용자가 일치하지 않는 경우
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
     * 사용자를 삭제한다.
     * @param id 삭제할 사용자의 id
     * @return 삭제한 사용자
     * @throws UserNotFoundException 삭제할 사용자를 찾지 못한 경우
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
