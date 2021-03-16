package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 사용자 관련 비즈니스 로직을 담당합니다.
 */
@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(Mapper mapper, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * 주어진 id에 해당하는 사용자를 반환합니다.
     *
     * @param id 해당 사용자의 식별자
     * @return 해당 id를 갖는 사용자
     * @throws UserNotFoundException 주어진 id에 해당하는 사용자가 없을 경우
     */
    public User getUser(Long id) {
        return findUser(id);
    }

    /**
     * 새로운 사용자를 등록하고 반환합니다.
     *
     * @param createRequest 등록할 정보
     * @return 등록된 사용자
     * @throws UserEmailDuplicationException 등록하려는 이메일이 이미 존재하는 경우
     */
    public User createUser(UserCreateRequestDto createRequest) {
        String email = createRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }

        User user = userRepository.save(
                mapper.map(createRequest, User.class));

        user.changePassword(createRequest.getPassword());

        Role role = roleRepository.save(new Role(user.getId(), "USER"));

        return user;
    }

    /**
     * 주어진 id에 해당하는 사용자의 정보를 수정합니다.
     *
     * @param id 수정하고자 하는 타겟 사용자의 식별자
     * @param updateRequest 수정할 정보
     * @param userId 현재 수정하는 행위의 주체가 되는 사용자의 식별자
     * @return 수정된 사용자
     * @throws AccessDeniedException 타겟 사용자와 현재 사용자가 일치하지 않을 경우
     * @throws UserNotFoundException 주어진 id에 해당하는 사용자가 존재하지 않을 경우
     */
    public User updateUser(
            Long id,
            UserUpdateRequestDto updateRequest,
            Long userId
    ) throws AccessDeniedException {
        if (id != userId) {
            throw new AccessDeniedException("Access Denied");
        }

        User user = findUser(id);

        user.updateWith(mapper.map(updateRequest, User.class));
        user.changePassword(updateRequest.getPassword());

        return user;
    }

    /**
     * 주어진 id에 해당하는 사용자를 삭제합니다.
     *
     * @param id 삭제할 사용자의 식별자
     * @return 삭제된 사용자
     */
    public User deleteUser(Long id) {
        User user = findUser(id);

        userRepository.delete(user);

        return user;
    }

    /**
     * 주어진 id에 해당하는 사용자를 반환합니다.
     *
     * @param id 해당 사용자의 식별자
     * @return 해당 id를 갖는 사용자
     */
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
