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
 * 회원과 관련된 비즈니스 로직을 담당합니다.
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
     * 주어진 회원 정보로 회원을 생성하고, 생성된 회원을 리턴합니다.
     *
     * @param registrationData 회원 정보
     * @return 생성된 회원
     * @throws UserEmailDuplicationException 회원 이메일이 중복될 경우
     */
    public User registerUser(UserRegistrationData registrationData)
            throws UserEmailDuplicationException {
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
     * 주어진 id에 해당하는 회원을 전달받은 회원 수정 정보로 변경하고,
     * 변경된 회원을 리턴합니다.
     *
     * @param id 변경하고자 하는 회원 식별자
     * @param modificationData 회원 수정 정보
     * @param userId 인증된 회원 식별자
     * @return 변경된 회원
     * @throws AccessDeniedException 변경하려고 하는 회원이 자기 자신이 아닐 경우
     * @throws UserNotFoundException 회원을 찾을 수 없는 경우
     */
    public User updateUser(Long id,
                           UserModificationData modificationData,
                           Long userId)
            throws AccessDeniedException, UserNotFoundException {
        if (!id.equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 주어진 id에 해당하는 회원을 삭제하고, 삭제된 회원을 리턴합니다.
     *
     * @param id 회원의 식별자
     * @return 삭제된 회원
     * @throws UserNotFoundException 회원을 찾을 수 없는 경우
     */
    public User deleteUser(Long id) throws UserNotFoundException {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
