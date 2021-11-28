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
import javax.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원 관련 처리 담당.
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
     * 회원 등록 정보로 회원을 생성하고, 리턴합니다.
     *
     * @param registrationData 회원 등록 정보
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
     * 식별자로 회원을 찾아 회원 정보를 수정하고, 리턴합니다.
     *
     * @param id               식별자
     * @param modificationData 수정할 회원 정보
     * @param userId           회원 id
     * @return 수정된 회원
     * @throws AccessDeniedException 식별자와 회원 id가 일치하지 않은 경우
     * @throws UserNotFoundException 식별자로 회원을 찾을 수 없을 경우
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
     * 식별자로 회원을 찾아 삭제하고, 리턴합니다.
     *
     * @param id 식별자
     * @return 삭제된 회원
     * @throws UserNotFoundException 식별자로 회원을 찾을 수 없을 경우
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 식별자로 회원을 찾아 리턴합니다.
     *
     * @param id 식별자
     * @return 찾은 회원
     * @throws UserNotFoundException 식별자로 회원을 찾을 수 없을 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
}
