package com.codesoom.assignment.user.application;

import com.codesoom.assignment.user.domain.Role;
import com.codesoom.assignment.user.domain.RoleRepository;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import com.codesoom.assignment.user.dto.UserModificationData;
import com.codesoom.assignment.user.dto.UserRegistrationData;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 사용자 정보를 다룬다.
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
     * 사용자를 등록하고, 등록된 정보를 리턴한다.
     *
     * @param registrationData 등록할 사용자 정보
     * @return 등록된 사용자 정보
     */
    public User registerUser(UserRegistrationData registrationData) {
        String email = registrationData.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }

        User source = User.of(registrationData.getName(),
                registrationData.getEmail());
        final User user = userRepository.save(source);

        user.changePassword(registrationData.getPassword(), passwordEncoder);

        roleRepository.save(new Role(user.getId(), "USER"));

        return user;
    }

    /**
     * 등록된 사용자 정보를 갱신하고, 생신된 정보를 리턴합니다.
     *
     * @param userId 등록된 사용자 식별자
     * @param modificationData 갱신할 사용자 정보
     * @return 갱신된 사용자 정보
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
     * 등록된 사용자를 삭제한다.
     *
     * @param id 등록된 사용자 식별자
     * @return 삭제된 사용자 id
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
