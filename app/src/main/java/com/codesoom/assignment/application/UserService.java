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
 * 유저에 관한 비즈니스 로직을 제공한다.
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
     * 유저 정보로 유저를 생성 후 반환한다.
     *
     * @param registrationData 유저 정보
     * @return 생성한 유저
     * @throws UserEmailDuplicationException 유저 정보에 있는 이메일이 이미 저장소에 있는 경우
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
     * 수정 대상 유저 식별자에 해당하는 유저를 전달받은 유저정보로 수정후 반환한다.
     *
     * @param id 수정 대상 유저 식별자
     * @param modificationData 수정 유저 정보
     * @param userId 수정 요청 유저 식별자
     * @return 수정된 유저
     * @throws AccessDeniedException 수정 대상 유저 식별자와 수정 요청 유저 식별자가 다른 경우
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
     * 유저식별자에 해당하는 유저를 삭제후 반환한다.
     *
     * @param id 유저 식별자
     * @return 삭제될 유저
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 유저 식별자에 해당하는 유저를 반환한다.
     *
     * @param id 유저식별자
     * @return 식별자 해당 유저
     * @throws UserNotFoundException 유저 식별자에 해당하는 유저를 찾을 수 없을 때
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
