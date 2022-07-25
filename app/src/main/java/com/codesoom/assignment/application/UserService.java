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
 * User 에 대한 비즈니스 로직
 */
@Service
@Transactional
public class UserService {
    /**
     * User 데이터 저장소
     */
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
     * 주어진 User 를 저장하고 저장된 User 반환
     * User 의 비밀번호는 암호화 과정을 거친 후 저장하고, 권한은 "USER" 로 저장한다.
     *
     * @param registrationData User 등록 정보
     * @return 등록된 User 정보
     * @throws UserEmailDuplicationException User 이메일이 중복된 경우
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
     * 주어진 id 와 일치하는 User 를 UserModificationData 로 수정하고 수정된 User 반환
     *
     * @param id 요청 id
     * @param modificationData 수정된 User 정보
     * @param userId 수정할 대상 User id
     * @return 수정된 User
     * @throws AccessDeniedException 주어진 id 와 대상 User id 가 일치하지 않는 경우
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
     * 주어진 id 와 일치하는 User 삭제하고 삭제된 User 반환
     *
     * @param id User 식별자
     * @return 삭제된 User
     * @throws UserNotFoundException 주어진 id 와 일치하는 User 를 찾을 수 없는 경우
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 주어진 id 와 일치하는 User 를 찾을 수 있으면 반환하고,
     * 찾을 수 없다면 User 를 찾을 수 없다는 예외를 던진다.
     *
     * @param id User 식별자
     * @return 주어진 id 와 일치하는 User
     * @throws UserNotFoundException 주어진 id 와 일치하는 User 를 찾을 수 없는 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
