package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserEmailDuplicatedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.security.UserAuthentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/** 사용자에 대한 요청을 수행한다. */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 전체 사용자 목록을 리턴한다.
     *
     * @return 저장되어 있는 전체 사용자 목록
     */
    public List<UserResultData> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResultData::of)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 사용자를 저장하고 해당 사용자를 리턴한다.
     *
     * @param userCreateData - 저장하고자 하는 새로운 사용자
     * @return 저장 된 사용자
     * @throws UserEmailDuplicatedException 만약
     *         주어진 이메일이 이미 존재하는 경우
     */
    public UserResultData createUser(UserCreateData userCreateData) {
        String email = userCreateData.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicatedException(email);
        }

        User user = userCreateData.toEntity();
        User savedUser = userRepository.save(user);
        user.updatePassword(userCreateData.getPassword(), passwordEncoder);
        roleRepository.save(new Role(savedUser.getEmail(), "USER")) ;

        return UserResultData.of(savedUser);
    }

    /**
     * 주어진 식별자에 해당하는 서용자를 수정하고 해당 사용자를 리턴한다.
     *
     * @param id - 수정하고자 하는 사용자의 식별자
     * @param userUpdateData - 수정하고자 하는 새로운 사용자
     * @param authentication - 수정을 하려고하는 사용자 정보
     * @return 수정 된 사용자
     * @throws UserNotFoundException 만약
     *         주어진 {@code id}에 해당하는 사용자가 저장되어 있지 않은 경우
     * @throws AccessDeniedException 만약
     *         주어진 {@code email}과 수정하려는 이메일이 다른 경우
     */
    public UserResultData updateUser(
            Long id,
            UserUpdateData userUpdateData,
            UserAuthentication authentication
    ) {
        User user = getUser(id);
        String userEmail = authentication.getEmail();
        if (!user.isSame(userEmail)) {
            throw new AccessDeniedException("Access denied");
        }

        user.updateName(userUpdateData.getName());
        user.updatePassword(userUpdateData.getPassword(), passwordEncoder);

        return UserResultData.of(user);
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 삭제하고 해당 사용자를 리턴한다.
     *
     * @param id - 삭제하고자 하는 사용자의 식별자
     * @return 삭제 된 사용자
     * @throws UserNotFoundException 만약
     *          주어진 {@code id}에 해당하는 사용자가 저장되어 있지 않은 경우
     */
    public UserResultData deleteUser(Long id) {
        User user = getUser(id);

        user.delete();

        return UserResultData.of(user);
    }

    /**
     * 주어진 식별자에 해당하는 사용자를 리턴한다.
     *
     * @param id - 조회하고자 하는 사용자의 식별자
     * @return 주어진 {@code id}에 해당하는 사용자
     * @throws UserNotFoundException 만약
     *         주어진 {@code id}에 해당하는 사용자가 저장되어 있지 않은 경우
     */
    public User getUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
