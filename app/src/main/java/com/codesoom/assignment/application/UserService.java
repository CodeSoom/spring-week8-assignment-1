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
 * 사용자 서비스
 *
 * @author newoo (newoo4297@naver.com)
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
     * 계정을 등록하고, 등록된 계정을 반환한다.
     *
     * @param registrationData 등록할 계정 데이터
     * @return 등록된 계정
     * @throws UserEmailDuplicationException 사용자의 이메일이 이미 존재할 경우
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
     * 주어진 식별자에 해당하는 계정을 수정하고, 수정된 계정을 반환한다.
     *
     * @param id 수정할 계정의 식별자
     * @param modificationData 수정할 계정 데이터
     * @param userId 수정을 요청한 계정의 식별자
     * @return 수정된 계정
     * @throws AccessDeniedException 계정정보 수정 권한이 없을 경우
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
     * 주어진 식별자에 해당하는 계정을 삭제하고, 삭제된 계정을 반환한다.
     *
     * @param id 삭제할 계정의 식별자
     * @return 삭제된 계정
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 주어진 식별자로 계정을 찾고, 찾은 계정을 반환한다.
     *
     * @param id 찾을 계정의 식별자
     * @return 찾은 계정
     * @throws UserNotFoundException 주어진 식별자에 해당하는 계정이 없을 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
