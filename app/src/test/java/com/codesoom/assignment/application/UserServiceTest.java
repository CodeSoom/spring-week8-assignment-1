package com.codesoom.assignment.application;

import com.codesoom.assignment.application.user.UserDeleteInterface;
import com.codesoom.assignment.application.user.UserDeleteService;
import com.codesoom.assignment.application.user.UserFindInterface;
import com.codesoom.assignment.application.user.UserFindService;
import com.codesoom.assignment.application.user.UserRegisterInterface;
import com.codesoom.assignment.application.user.UserRegisterService;
import com.codesoom.assignment.application.user.UserUpdateInterface;
import com.codesoom.assignment.application.user.UserUpdateService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.RoleRepository;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.error.UserEmailDuplicationException;
import com.codesoom.assignment.error.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private static final Long DELETED_USER_ID = 200L;

    private UserFindInterface userFindService;
    private UserRegisterInterface userRegisterService;
    private UserDeleteInterface userDeleteService;
    private UserUpdateInterface userUpdateService;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        userFindService = new UserFindService(userRepository);
        userRegisterService = new UserRegisterService(userRepository,
                                                        roleRepository,
                                                        mapper,
                                                        passwordEncoder);
        userDeleteService = new UserDeleteService(userFindService);
        userUpdateService = new UserUpdateService(userFindService , mapper);

        given(userRepository.existsByEmail(EXISTED_EMAIL_ADDRESS))
                .willThrow(new UserEmailDuplicationException(
                        EXISTED_EMAIL_ADDRESS));

        given(userRepository.save(any(User.class))).will(invocation -> {
            User source = invocation.getArgument(0);
            return User.builder()
                    .id(13L)
                    .email(source.getEmail())
                    .name(source.getName())
                    .build();
        });

        given(userRepository.findByIdAndDeletedIsFalse(1L))
                .willReturn(Optional.of(
                        User.builder()
                                .id(1L)
                                .email(EXISTED_EMAIL_ADDRESS)
                                .name("Tester")
                                .password("test")
                                .build()));

        given(userRepository.findByIdAndDeletedIsFalse(100L))
                .willReturn(Optional.empty());

        given(userRepository.findByIdAndDeletedIsFalse(DELETED_USER_ID))
                .willReturn(Optional.empty());
    }

    @Test
    void registerUser() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email("tester@example.com")
                .name("Tester")
                .password("test")
                .build();

        User user = userRegisterService.registerUser(registrationData);

        assertThat(user.getId()).isEqualTo(13L);
        assertThat(user.getEmail()).isEqualTo("tester@example.com");
        assertThat(user.getName()).isEqualTo("Tester");

        verify(userRepository).save(any(User.class));
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void registerUserWithDuplicatedEmail() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email(EXISTED_EMAIL_ADDRESS)
                .name("Tester")
                .password("test")
                .build();

        assertThatThrownBy(() -> userRegisterService.registerUser(registrationData))
                .isInstanceOf(UserEmailDuplicationException.class);

        verify(userRepository).existsByEmail(EXISTED_EMAIL_ADDRESS);
    }

    @Test
    void updateUserWithExistedId() throws AccessDeniedException {
        UserModificationData modificationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();

        Long userId = 1L;
        User user = userUpdateService.updateUser(userId, modificationData, userId);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo(EXISTED_EMAIL_ADDRESS);
        assertThat(user.getName()).isEqualTo("TEST");

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void updateUserWithNotExistedId() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();

        Long userId = 100L;
        assertThatThrownBy(
                () -> userUpdateService.updateUser(userId, modificationData, userId)
        )
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }


    @Test
    void updateUserWithDeletedId() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();

        Long userId = DELETED_USER_ID;
        assertThatThrownBy(
                () -> userUpdateService.updateUser(userId, modificationData, userId)
        )
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
    }

    @Test
    void updateUserByOthersAccess() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("TEST")
                .password("TEST")
                .build();

        Long targetUserId = 1L;
        Long currentUserId = 2L;

        assertThatThrownBy(() -> {
            userUpdateService.updateUser(
                    targetUserId, modificationData, currentUserId);
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void deleteUserWithExistedId() {
        User user = userDeleteService.deleteUser(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.isDeleted()).isTrue();

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void deleteUserWithNotExistedId() {
        assertThatThrownBy(() -> userDeleteService.deleteUser(100L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }

    @Test
    void deleteUserWithDeletedId() {
        assertThatThrownBy(() -> userDeleteService.deleteUser(DELETED_USER_ID))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
    }
}
