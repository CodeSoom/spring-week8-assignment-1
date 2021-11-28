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
     * 유저를 생성하고 생성한 유저 정보를 리턴한다.
     * @param registrationData
     * @return 생성한 유저 Data
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
     * id에 해당하는 유저 정보를 찾아 정보를 수정한다.
     * @param id
     * @param modificationData
     * @return 수정한 유저 정보
     * @throws id에 해당하는 상품을 찾지 못할 경우 AccessDeniedException
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
     * id에 해당하는 유저를 찾아 삭제한다 - 유저 탈퇴
     * @param id
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * id에 해당하는 유저를 찾아 리턴한다.
     * @param id
     * @return id에 해당하는 유저
     * @throws id에 해당하는 상품을 찾지 못할 경우 UserNotFoundException
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
