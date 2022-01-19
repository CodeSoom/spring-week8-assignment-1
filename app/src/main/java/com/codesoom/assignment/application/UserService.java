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
 * 사용자를 관리한다.
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
     * 사용자 등록 정보를 받아 저장하고 리턴한다.
     * 사용자의 비밀번호는 인코딩하여 저장되며 권한은 "USER"로 저장한다.
     * @param registrationData 사용자 등록 정보
     * @return 등록된 사용자
     * @throws UserEmailDuplicationException 사용자 이메일이 중복될 경우
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
     * 주어진 id의 사용자의 정보를 수정된 정보로 업데이트하고 리턴한다.
     * @param id 요청 id
     * @param modificationData 수정된 정보
     * @param userId 수정 대상 사용자 id
     * @return 수정된 사용자
     * @throws AccessDeniedException 주어진 id와 대상 사용자 id가 일치하지 않을 경우
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
     * 주어진 id의 사용자를 삭제하고 리턴한다.
     * @param id 사용자 식별자
     * @return 삭제된 사용자
     * @throws UserNotFoundException id에 해당하는 사용자가 없는 경우
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 주어진 id의 사용자를 찾아서 리턴한다.
     * @param id
     * @return id에 해당하는 사용자
     * @throws UserNotFoundException id에 맞는 사용자를 찾을 수 없는 경우
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
