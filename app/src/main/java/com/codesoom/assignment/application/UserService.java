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
 * 회원 관련 비즈니스 로직을 담당한다.
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
     * 주어진 회원 정보로 회원을 생성해 저장하고 생성한 회원을 반환한다.
     *
     * @param registrationData 회원 정보
     * @return 생성한 회원
     * @throws UserEmailDuplicationException 회원 이메일이 중복인 경우
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

        roleRepository.save(new Role(user.getId(), "USER" ));

        return user;
    }

    /**
     * 주어진 id에 해당하는 회원을 주어진 수정 정보로 수정하고 수정한 회원을 반환한다.
     *
     * @param id 수정할 회원 식별자
     * @param modificationData 회원 수정 정보
     * @param userId 인증한 회원 식별자
     * @return 변경한 회원
     * @throws AccessDeniedException 변경할 회원이 자신이 아닌 경우
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
     * 주어진 id에 해당하는 회원을 삭제하고 삭제한 회원을 반환한다.
     *
     * @param id 삭제할 회원의 식별자
     * @return 삭제한 회원
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
