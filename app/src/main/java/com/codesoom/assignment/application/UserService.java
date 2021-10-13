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
 * 회원 관리 서비스를 제공한다.
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
     * 회원을 등록하고, 등록 된 회원 정보를 반환한다.
     *
     * @param registrationData 등록 할 회원 정보
     * @return 등록된 회원 정보
     * @throws UserEmailDuplicationException 등록 할 회원의 이메일이 이미 등록 된 회원일 경우
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
     * 식별자로 회원을 찾아 정보를 수정하고, 수정 된 회원 정보를 반환한다.
     *
     * @param id 수정 할 회원 식별자
     * @param modificationData 수정 할 회원 정보
     * @param userId 수정 요청을 한 회원 식별자
     * @return 수정 된 회원 정보
     * @throws AccessDeniedException 수정하려는 회원과 수정 할 회원의  식별자가 일치하지 않는 경우
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
     * 회원 정보를 삭제하고, 삭제 된 회원 정보를 반환한다.
     *
     * @param id 식별자
     * @return 삭제된 회원 정보
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
