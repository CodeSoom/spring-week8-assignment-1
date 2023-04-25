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
 * Service for User
 *
 * @author sim
 */
@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * UserService 생성자를 호출한다.
     *
     * @param dozerMapper - Bean Object Mapper
     * @param userRepository - 유저 리포지토리
     * @param roleRepository - 권한 리포지토리
     * @param passwordEncoder - 비밀번호 암호화 인코더
     */
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
     * 유저를 등록한다.
     * 추가적으로 비밀번호 암호화 및 USER 권한을 부여한다.
     *
     * @param registrationData - 유저 등록 정보 Object
     * @throws UserEmailDuplicationException - 이미 존재하는 이메일일 경우 예외 발생
     * @return 등록된 유저 엔티티
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
     * 유저를 수정한다.
     *
     * @param id - Client에 대한 유저 ID.
     * @param modificationData - 유저 수정 정보 Object.
     * @param userId - 수정하고자 하는 유저 ID.
     * @return 수정된 User 엔티티.
     * @throws AccessDeniedException - 타 유저의 정보 수정 시 예외 발생.
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
     * 유저를 삭제한다.
     *
     * @param id - 삭제하고자 하는 유저 ID.
     * @return 삭제된 유저 엔티티.
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 유저 조회
     * 
     * @param id - 조회하고자 하는 유저 ID
     * @return 조회된 User 엔티티
     * @throws UserNotFoundException - 조회하고자 하는 유저 ID가 없을 경우 발생.
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
