package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserBadRequestException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.security.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
class UserControllerTest {
    private static final String MY_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJleGlzdGVkRW1haWwifQ.UQodS3elf3Cu4g0PDFHqVloFbcKHHmTTnk0jGmiwPXY";
    private static final String OTHER_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJjcmVhdGVkRW1haWwifQ.8mfjGjxD-qmkIXjJ-iznb38c92xijMUJOs8GKt8GYbU";
    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJhZG1pbkVtYWlsIn0.P2g7CPe8Rx897aj0OncPab-f0cNwvPuqdtdXJfXAex4";
    private static final String SETUP_USER_NAME = "setUpName";
    private static final String SETUP_USER_EMAIL = "setUpdEmail";
    private static final String SETUP_USER_PASSWORD = "setUpPassword";

    private static final String CREATE_USER_NAME = "createdName";
    private static final String CREATE_USER_EMAIL = "createdEmail";
    private static final String CREATE_USER_PASSWORD = "createdPassword";

    private static final String UPDATE_USER_NAME = "updatedName";
    private static final String UPDATE_USER_PASSWORD = "updatedPassword";

    private static final Long EXISTED_ID = 1L;
    private static final Long CREATED_ID = 2L;
    private static final Long DELETE_ID = 1L;
    private static final Long NOT_EXISTED_ID = 100L;

    private static final String MY_EMAIL = SETUP_USER_EMAIL;
    private static final String OTHER_EMAIL = CREATE_USER_EMAIL;
    private static final String ADMIN_EMAIL = "adminEmail";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    private List<User> users;
    private User setUpUser;
    private User createUser;

    private List<UserResultData> resultUsers;
    private UserResultData setupUserResult;
    private UserResultData createUserResult;

    @BeforeEach
    void setUp() {
        setUpUser = User.builder()
                .id(EXISTED_ID)
                .name(SETUP_USER_NAME)
                .email(SETUP_USER_EMAIL)
                .password(SETUP_USER_PASSWORD)
                .build();

        createUser = User.builder()
                .id(CREATED_ID)
                .name(CREATE_USER_NAME)
                .email(CREATE_USER_EMAIL)
                .password(CREATE_USER_PASSWORD)
                .build();

        users = List.of(setUpUser, createUser);

        setupUserResult = UserResultData.of(setUpUser);
        createUserResult = UserResultData.of(createUser);
        resultUsers = List.of(setupUserResult, createUserResult);
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_lists {
        @Test
        @DisplayName("전체 사용자 목록과 OK를 리턴한다")
        void itReturnsListOfUsersAndOKHttpStatus() throws Exception {
            given(userService.getUsers()).willReturn(resultUsers);

            mockMvc.perform(
                    get("/users")
            )
                    .andDo(print())
                    .andExpect(content().string(containsString("\"id\":" + EXISTED_ID)))
                    .andExpect(content().string(containsString("\"id\":" + CREATED_ID)))
                    .andExpect(status().isOk());

            verify(userService).getUsers();
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자와 OK를 리턴한다")
            void itReturnsUserAndOkHttpStatus() throws Exception {
                given(userService.getUser(givenExistedId)).willReturn(setUpUser);

                mockMvc.perform(
                        get("/users/"+givenExistedId)
                )
                        .andDo(print())
                        .andExpect(content().string(containsString("{\"id\":" + EXISTED_ID)))
                        .andExpect(status().isOk());

                verify(userService).getUser(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디로 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던지고 NOT_FOUND를 리턴한다")
            void itThrowsNotFoundExceptionAndReturnsNOT_FOUNDHttpStatus() throws Exception {
                given(userService.getUser(givenNotExistedId))
                        .willThrow(new UserNotFoundException(givenNotExistedId));

                mockMvc.perform(
                        get("/users/"+givenNotExistedId)
                )
                        .andExpect(content().string(containsString("User not found")))
                        .andExpect(status().isNotFound());

                verify(userService).getUser(givenNotExistedId);
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Nested
        @DisplayName("만약 사용자가 주어진다면")
        class Context_WithUser {
            @Test
            @DisplayName("사용자를 저장하고 저장된 사용자와 CREATED를 리턴한다")
            void itSavesUserAndReturnsUser() throws Exception {
                given(userService.createUser(any(UserCreateData.class)))
                        .will(invocation -> {
                            UserCreateData userCreateData = invocation.getArgument(0);
                            return UserResultData.builder()
                                    .id(CREATED_ID)
                                    .name(userCreateData.getName())
                                    .email(userCreateData.getEmail())
                                    .password(userCreateData.getPassword())
                                    .build();
                        });

                mockMvc.perform(
                        post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"createdName\",\"email\":\"createdEmail\",\"password\":\"createdPassword\"}")
                )
                        .andExpect(jsonPath("id").value(createUserResult.getId()))
                        .andExpect(jsonPath("name").value(createUserResult.getName()))
                        .andExpect(jsonPath("email").value(createUserResult.getEmail()))
                        .andExpect(jsonPath("password").value(createUserResult.getPassword()))
                        .andExpect(status().isCreated());

                verify(userService).createUser(any(UserCreateData.class));
            }
        }

        @Nested
        @DisplayName("만약 비어있는 값이 주어진다면")
        class Context_WithEmpty {
            @Test
            @DisplayName("사용자 요청이 잘못 되었다는 예외를 던지고 BAD_REQUEST를 리턴한다")
            void itThrowsUserBadRequestExceptionAndReturnsBAD_REQUESTHttpStatus() throws Exception {
                mockMvc.perform(
                        post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
                )
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디와 수정 할 사용자와 본인 이메일이 주어진다면")
        class Context_WithExistedIdAndUser {
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
            @DisplayName("주어진 아이디에 해당하는 사용자를 수정하고 수정된 사용자와 OK를 리턴한다")
            void itUpdatesUserAndReturnUpdatedUserAndOKHttpStatus() throws Exception {
                given(authenticationService.parseToken(MY_TOKEN)).willReturn(MY_EMAIL);
                given(authenticationService.roles(givenExistedEmail)).willReturn(Arrays.asList(new Role("USER")));
                given(userService.updateUser(eq(givenExistedId), any(UserUpdateData.class), eq(authentication)))
                        .will(invocation -> {
                            Long id = invocation.getArgument(0);
                            UserUpdateData userUpdateData = invocation.getArgument(1);
                            return UserResultData.builder()
                                    .id(id)
                                    .name(userUpdateData.getName())
                                    .password(userUpdateData.getPassword())
                                    .build();
                        });

                mockMvc.perform(
                        patch("/users/" + givenExistedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + MY_TOKEN)
                                .content("{\"name\":\"updatedName\",\"password\":\"updatedPassword\"}")
                )
                        .andDo(print())
                        .andExpect(jsonPath("name").value(userUpdateData.getName()))
                        .andExpect(jsonPath("password").value(userUpdateData.getPassword()))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedIdAndUser {
            private final Long givenNotExistedId = NOT_EXISTED_ID;
            private UserAuthentication authentication;
            private final String givenExistedEmail = MY_EMAIL;

            @BeforeEach
            void setUp() {
                authentication = UserAuthentication.builder()
                        .email(givenExistedEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던지고 NOT_FOUND를 리턴한다")
            void itThrowsUserNotFoundExceptionAndReturnsNOT_FOUNDHttpStatus() throws Exception {
                given(authenticationService.parseToken(MY_TOKEN)).willReturn(MY_EMAIL);
                given(authenticationService.roles(givenExistedEmail)).willReturn(List.of(new Role("USER")));
                given(userService.updateUser(eq(givenNotExistedId), any(UserUpdateData.class), eq(authentication)))
                        .willThrow(new UserNotFoundException(givenNotExistedId));

                mockMvc.perform(
                        patch("/users/"+givenNotExistedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + MY_TOKEN)
                                .content("{\"name\":\"createdUser\",\"password\":\"createdPassword\"}")
                )
                        .andDo(print())
                        .andExpect(status().isNotFound());

                verify(userService).updateUser(eq(givenNotExistedId), any(UserUpdateData.class), eq(authentication));
            }
        }

        @Nested
        @DisplayName("만약 비어있는 값이 주어진다면")
        class Context_WithEmpty {
            private final Long givenExistedId = EXISTED_ID;
            private UserAuthentication authentication;
            private final String givenMyEmail = MY_EMAIL;

            @BeforeEach
            void setUp() {
                authentication = UserAuthentication.builder()
                        .email(givenMyEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("사용자 요청이 잘못 되었다는 예외를 던지고 BAD_REQUEST를 리턴한다")
            void itThrowsUserBadRequestExceptionAndReturnsBAD_REQUESTHttpStatus() throws Exception {
                given(userService.updateUser(eq(givenExistedId), any(UserUpdateData.class), eq(authentication)))
                        .willThrow(new UserBadRequestException());

                mockMvc.perform(
                        patch("/users/" + givenExistedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + MY_TOKEN)
                                .content("{}")
                )
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("만약 토큰이 주어지지 않는다면")
        class Context_WithoutToken {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("권한이 없다는 예외를 던지고 UNAUTHORIZED를 리턴한다")
            void itThrowsUnauthorizedExceptionAndReturnsUNAUTHORIZEDHttpStatus() throws Exception {
                mockMvc.perform(
                        patch("/users/" + givenExistedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"createdUser\",\"password\":\"createdPassword\"}")
                )
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("만약 다른 사용자의 토큰이 주어진다면")
        class Context_WithOtherToken {
            private final Long givenExistedId = EXISTED_ID;
            private UserAuthentication authentication;
            private final String givenNotExistedEmail = OTHER_EMAIL;

            @BeforeEach
            void setUp() {
                authentication = UserAuthentication.builder()
                        .email(givenNotExistedEmail)
                        .roles(List.of(new Role("USER")))
                        .build();
            }

            @Test
            @DisplayName("접근이 거부되었다는 예외를 던지고 FORBIDDEN를 리턴한다")
            void itThrowsAccessDeniedExceptionAndReturnsFORBIDDENHttpStatus() throws Exception {
                given(authenticationService.parseToken(OTHER_TOKEN)).willReturn(OTHER_EMAIL);
                given(authenticationService.roles(givenNotExistedEmail)).willReturn(Arrays.asList(new Role("USER")));
                given(userService.updateUser(eq(givenExistedId), any(UserUpdateData.class), eq(authentication)))
                        .willThrow(new AccessDeniedException("Access denied"));

                mockMvc.perform(
                        patch("/users/" + givenExistedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + OTHER_TOKEN)
                                .content("{\"name\":\"createdUser\",\"password\":\"createdPassword\"}")
                )
                        .andDo(print())
                        .andExpect(status().isForbidden());

                verify(userService).updateUser(eq(givenExistedId), any(UserUpdateData.class), eq(authentication));
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;
            private UserResultData userResultData;

            @BeforeEach
            void setUp() {
                setUpUser.delete();
                userResultData = UserResultData.of(setUpUser);
            }

            @Test
            @DisplayName("주어진 아이디에 해당하는 사용자를 삭제하고 삭제된 사용자와 NO_CONTENT를 리턴한다")
            void itDeletesUserAndReturnsDeletedUserAndNO_CONTENTHttpStatus() throws Exception {
                given(authenticationService.roles(ADMIN_EMAIL))
                        .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
                given(authenticationService.parseToken(ADMIN_TOKEN)).willReturn(ADMIN_EMAIL);
                given(userService.deleteUser(eq(givenExistedId))).willReturn(userResultData);

                mockMvc.perform(
                        delete("/users/" + givenExistedId)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                        .andDo(print())
                        .andExpect(content().string(containsString("\"deleted\":true")))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 사용자의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던지고 NOT_FOUND를 리턴한다")
            void itThrowsNotFoundExceptionAndReturnsNOT_FOUNDHttpStatus() throws Exception {
                given(authenticationService.roles(ADMIN_EMAIL))
                        .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
                given(authenticationService.parseToken(ADMIN_EMAIL)).willReturn(ADMIN_EMAIL);
                given(userService.deleteUser(givenNotExistedId))
                        .willThrow(new UserNotFoundException(givenNotExistedId));

                mockMvc.perform(
                        delete("/users/"+givenNotExistedId)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 이미 삭제된 사용자의 아이디가 주어진다면")
        class Context_WithDeletedId {
            private final Long givenDeletedId = DELETE_ID;

            @BeforeEach
            void setUp() {
                setUpUser.delete();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던지고 NOT_FOUND를 리턴한다")
            void itThrowsNotFoundExceptionAndNOT_FOUNDHttpStatus() throws Exception {
                given(authenticationService.roles(ADMIN_EMAIL))
                        .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
                given(authenticationService.parseToken(ADMIN_TOKEN)).willReturn(ADMIN_EMAIL);
                given(userService.deleteUser(givenDeletedId))
                        .willThrow(new UserNotFoundException(givenDeletedId));

                mockMvc.perform(
                        delete("/users/"+DELETE_ID)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(givenDeletedId);
            }
        }

        @Nested
        @DisplayName("만약 토큰이 주어지지 않는다면")
        class Context_WithoutToken {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("권한이 없다는 예외를 던지고 UNAUTHORIZED를 리턴한다")
            void itThrowsUnauthorizedExceptionAndReturnsUNAUTHORIZEDHttpStatus() throws Exception {
                mockMvc.perform(
                        delete("/users/" + givenExistedId)
                )
                        .andDo(print())
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("만약 admin 계정이 주어지지 않는다면")
        class Context_WithoutAdminRole {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("권한이 없다는 예외를 던지고 UNAUTHORIZED를 리턴한다")
            void itThrowsUnauthorizedExceptionAndReturnsUNAUTHORIZEDHttpStatus() throws Exception {
                given(authenticationService.parseToken(MY_TOKEN)).willReturn(MY_EMAIL);

                mockMvc.perform(
                        delete("/users/" + givenExistedId)
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                        .andDo(print())
                        .andExpect(status().isForbidden());
            }
        }
    }
}
