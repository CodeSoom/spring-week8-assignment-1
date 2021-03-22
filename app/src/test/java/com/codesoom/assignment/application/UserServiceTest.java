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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("UserService 테스트")
class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private static final String SETUP_USER_NAME = "setUpName";
    private static final String EXISTED_USER_EMAIL = "setUpEmail";
    private static final String SETUP_USER_PASSWORD = "setUpPassword";
    private String setUpEncodedPassword;

    private static final String CREATE_USER_NAME = "createdName";
    private static final String CREATE_USER_EMAIL = "createdEmail";
    private static final String CREATE_USER_PASSWORD = "createdPassword";
    private String createEncodedPassword;

    private static final String UPDATE_USER_NAME = "updatedName";
    private static final String UPDATE_USER_PASSWORD = "updatedPassword";

    private static final Long EXISTED_ID = 1L;
    private static final Long CREATE_ID = 2L;
    private static final Long DELETED_ID = 1L;
    private static final Long NOT_EXISTED_ID = 100L;
    private static final String MY_EMAIL = EXISTED_USER_EMAIL;
    private static final String OTHER_EMAIL = CREATE_USER_EMAIL;

    private List<User> users;
    private User setUpUser;
    private User createUser;

    private List<UserResultData> resultUsers;
    private UserResultData setUpUserResultData;
    private UserResultData createUserResultData;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
        setUpEncodedPassword = passwordEncoder.encode(SETUP_USER_PASSWORD);
        createEncodedPassword = passwordEncoder.encode(CREATE_USER_PASSWORD);

        setUpUser = User.builder()
                .id(EXISTED_ID)
                .name(SETUP_USER_NAME)
                .email(EXISTED_USER_EMAIL)
                .password(setUpEncodedPassword)
                .build();

        createUser = User.builder()
                .id(CREATE_ID)
                .name(CREATE_USER_NAME)
                .email(CREATE_USER_EMAIL)
                .password(createEncodedPassword)
                .build();

        users = Arrays.asList(setUpUser, createUser);

        setUpUserResultData = UserResultData.of(setUpUser);
        createUserResultData = UserResultData.of(createUser);
        resultUsers = Arrays.asList(setUpUserResultData, createUserResultData);
    }

    @Nested
    @DisplayName("getUsers 메서드는")
    class Describe_getUsers {
        @Nested
        @DisplayName("만약 사용자 목록이 존재한다면")
        class Context_ExistsListOfUsers {
            @Test
            @DisplayName("저장되어 있는 사용자 목록을 리턴한다")
            void itReturnListOfUsers() {
                given(userRepository.findAll()).willReturn(users);

                List<UserResultData> list = userService.getUsers();
                assertThat(list).containsExactly(setUpUserResultData, createUserResultData);

                verify(userRepository).findAll();
            }
        }

        @Nested
        @DisplayName("만약 사용자 목록이 존재하지 않는다면")
        class Context_NotExistsListsOfUsers {
            @Test
            @DisplayName("비어있는 사용자 목록을 리턴한다")
            void itReturnsEmptyListOfUsers() {
                given(userRepository.findAll()).willReturn(List.of());

                List<UserResultData> list = userService.getUsers();

                assertThat(list).isEmpty();;

                verify(userRepository).findAll();
            }
        }
    }

    @Nested
    @DisplayName("getUser 메서드는")
    class Describe_getUser {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 리턴한다")
            void itReturnsUser() {
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                User user = userService.getUser(givenExistedId);

                assertThat(user.getId()).isEqualTo(setUpUser.getId());

                verify(userRepository).findByIdAndDeletedIsFalse(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던진다")
            void itThrowsUserNotFoundException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUser(givenNotExistedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 삭제된 사용자의 아이디가 주어진다면")
        class Context_WithDeletedId {
            private final Long givenDeletedId = DELETED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던진다")
            void itThrowsUserNotFoundException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenDeletedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUser(givenDeletedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenDeletedId);
            }
        }
    }

    @Nested
    @DisplayName("createUser 메서드는")
    class Describe_createUser {
        @Nested
        @DisplayName("만약 사용자가 주어진다면")
        class Context_WithUser {
            private UserCreateData userCreateData;

            @BeforeEach
            void setUp() {
                userCreateData = UserCreateData.builder()
                        .name(CREATE_USER_NAME)
                        .email(CREATE_USER_EMAIL)
                        .password(CREATE_USER_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("주어진 사용자를 저장하고 저장된 사용자를 리턴한다")
            void itSavesUserAndReturnsSavedUser() {
                given(userRepository.save(any(User.class))).willReturn(createUser);

                UserResultData createdUser = userService.createUser(userCreateData);

                assertThat(createdUser.getId()).isEqualTo(createUser.getId());
                assertThat(createdUser.getName()).isEqualTo(userCreateData.getName());
                assertThat(createdUser.getEmail()).isEqualTo(userCreateData.getEmail());

                verify(userRepository).save(any(User.class));
                verify(roleRepository).save(any(Role.class));
            }
        }

        @Nested
        @DisplayName("만약 이미 저장되어 있는 이메일을 가진 사용자가 주어진다면")
        class Context_WithUserWithDuplicatedEmail {
            private UserCreateData userCreateData;
            private final String givenExistedEmail = EXISTED_USER_EMAIL;

            @BeforeEach
            void setUp() {
                userCreateData = UserCreateData.builder()
                        .name(CREATE_USER_NAME)
                        .email(givenExistedEmail)
                        .password(CREATE_USER_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("이메일이 중복되었다는 예외를 던진다")
            void itThrowsEmailDuplicatedException() {
                given(userRepository.existsByEmail(givenExistedEmail)).willReturn(true);

                assertThatThrownBy(() -> userService.createUser(userCreateData))
                        .isInstanceOf(UserEmailDuplicatedException.class)
                        .hasMessageContaining("User email is already existed");

                verify(userRepository).existsByEmail(givenExistedEmail);
            }
        }
    }

    @Nested
    @DisplayName("updateUser 메서드는")
    class Describe_updateUser {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디와 수정 할 사용자와 본인 이메일이 주어진다면")
        class Context_WithExistedIdAndUserAndEmail {
            private final Long givenExistedId = EXISTED_ID;
            private UserUpdateData userUpdateData;
            private UserAuthentication authentication;
            private final String givenExistedEmail = MY_EMAIL;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();

                authentication = UserAuthentication.builder()
                        .email(givenExistedEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("사용자를 수정하고 수정된 사용자를 리턴한다")
            void itUpdatesUserAndReturnsUpdatedUser() {
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData updatedUser = userService.updateUser(givenExistedId, userUpdateData, authentication);

                assertThat(updatedUser.getId()).isEqualTo(givenExistedId);
                assertThat(updatedUser.getName()).isEqualTo(userUpdateData.getName());
                assertThat(updatedUser.getEmail()).isEqualTo(EXISTED_USER_EMAIL);

                verify(userRepository).findByIdAndDeletedIsFalse(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;
            private UserUpdateData userUpdateData;
            private UserAuthentication authentication;
            private final String givenExistedEmail = MY_EMAIL;


            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();

                authentication = UserAuthentication.builder()
                        .email(givenExistedEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던진다")
            void itThrowsUserNotFoundException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.updateUser(givenNotExistedId, userUpdateData, authentication))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 삭제된 사용자의 아이디가 주어진다면")
        class Context_WithDeletedId {
            private final Long givenDeletedId = DELETED_ID;
            private UserUpdateData userUpdateData;
            private UserAuthentication authentication;
            private final String givenExistedEmail = MY_EMAIL;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();

                setUpUser.delete();

                authentication = UserAuthentication.builder()
                        .email(givenExistedEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던진다")
            void itThrowsUserNotFoundException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenDeletedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.updateUser(givenDeletedId, userUpdateData, authentication))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenDeletedId);
            }
        }

        @Nested
        @DisplayName("만약 다른 사용자의 이메일이 주어진다면")
        class Context_WithOtherEmail {
            private final Long givenExistedId = EXISTED_ID;
            private UserUpdateData userUpdateData;
            private UserAuthentication authentication;
            private final String givenNotExistedEmail = OTHER_EMAIL;

            @BeforeEach
            void setUp() {
                userUpdateData = UserUpdateData.builder()
                        .name(UPDATE_USER_NAME)
                        .password(UPDATE_USER_PASSWORD)
                        .build();

                authentication = UserAuthentication.builder()
                        .email(givenNotExistedEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("접근이 거부되었다는 예외를 던진다")
            void itThrowsAccessDeniedException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                assertThatThrownBy(() -> userService.updateUser(givenExistedId, userUpdateData, authentication))
                        .isInstanceOf(AccessDeniedException.class)
                        .hasMessageContaining("Access denied");
            }
        }
    }

    @Nested
    @DisplayName("deleteUser 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 삭제하고 삭제된 사용자를 리턴한다")
            void itDeletesUserAndReturnsDeletedUser() {
                given(userRepository.findByIdAndDeletedIsFalse(givenExistedId)).willReturn(Optional.of(setUpUser));

                UserResultData userResultData = userService.deleteUser(givenExistedId);

                assertThat(userResultData.getId()).isEqualTo(setUpUser.getId());
                assertThat(userResultData.isDeleted()).isTrue();

                verify(userRepository).findByIdAndDeletedIsFalse(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 메세지 예외를 던진다")
            void itThrowsUserNotFoundException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenNotExistedId)).willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.deleteUser(givenNotExistedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 삭제된 사용자의 아이디가 주어진다면")
        class Context_WithDeletedId {
            private final Long givenDeletedId = DELETED_ID;

            @BeforeEach
            void setUp() {
                setUpUser.delete();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던진다")
            void itThrowsUserNotFoundException() {
                given(userRepository.findByIdAndDeletedIsFalse(givenDeletedId))
                        .willReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUser(givenDeletedId))
                        .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User not found");

                verify(userRepository).findByIdAndDeletedIsFalse(givenDeletedId);
            }
        }
    }
}
