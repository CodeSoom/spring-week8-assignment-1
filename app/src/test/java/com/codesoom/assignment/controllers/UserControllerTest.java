package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateData;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.dto.UserUpdateData;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.security.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
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
    private static final Long DELETE_ID = 3L;
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
    private User updatedUser;
    private User deletedUser;

    private List<UserResultData> resultUsers;
    private UserResultData setupUserData;
    private UserResultData createdUserData;
    private UserResultData updatedUserData;
    private UserResultData deletedUserData;
    private UserAuthentication invalidAuthentication;
    private UserAuthentication validAuthentication;

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

        updatedUser = User.builder()
                .id(EXISTED_ID)
                .name(UPDATE_USER_NAME)
                .email(SETUP_USER_EMAIL)
                .password(UPDATE_USER_PASSWORD)
                .build();

        deletedUser = User.builder()
                .id(DELETE_ID)
                .name(SETUP_USER_NAME)
                .email(SETUP_USER_EMAIL)
                .password(SETUP_USER_PASSWORD)
                .deleted(true)
                .build();

        validAuthentication = UserAuthentication.builder()
                .email(MY_EMAIL)
                .roles(List.of(new Role("USER")))
                .build();

        invalidAuthentication = UserAuthentication.builder()
                .email(OTHER_EMAIL)
                .roles(List.of(new Role("USER")))
                .build();

        users = List.of(setUpUser, createUser);

        setupUserData = UserResultData.of(setUpUser);
        createdUserData = UserResultData.of(createUser);
        updatedUserData = UserResultData.of(updatedUser);
        deletedUserData = UserResultData.of(deletedUser);
        resultUsers = List.of(setupUserData, createdUserData);

        given(userService.getUsers()).willReturn(resultUsers);

        given(userService.getUser(EXISTED_ID)).willReturn(setUpUser);

        given(userService.getUser(NOT_EXISTED_ID))
                .willThrow(new UserNotFoundException(NOT_EXISTED_ID));

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

        given(userService.updateUser(eq(EXISTED_ID), any(UserUpdateData.class), eq(validAuthentication)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    UserUpdateData userUpdateData = invocation.getArgument(1);
                    UserAuthentication authentication = invocation.getArgument(2);
                    return UserResultData.builder()
                            .id(id)
                            .name(userUpdateData.getName())
                            .email(authentication.getEmail())
                            .password(userUpdateData.getPassword())
                            .deleted(false)
                            .build();
                });

        given(userService.updateUser(eq(EXISTED_ID), any(UserUpdateData.class), eq(invalidAuthentication)))
                .willThrow(new AccessDeniedException("Access denied"));

        given(userService.updateUser(eq(NOT_EXISTED_ID), any(UserUpdateData.class), eq(validAuthentication)))
                .willThrow(new UserNotFoundException(NOT_EXISTED_ID));

        given(userService.deleteUser(eq(EXISTED_ID))).willReturn(deletedUserData);

        given(userService.deleteUser(NOT_EXISTED_ID))
                .willThrow(new UserNotFoundException(NOT_EXISTED_ID));

        given(userService.deleteUser(DELETE_ID))
                .willThrow(new UserNotFoundException(DELETE_ID));

        given(authenticationService.parseToken(MY_TOKEN)).willReturn(MY_EMAIL);
        given(authenticationService.parseToken(OTHER_TOKEN)).willReturn(OTHER_EMAIL);
        given(authenticationService.parseToken(ADMIN_TOKEN)).willReturn(ADMIN_EMAIL);

        given(authenticationService.roles(MY_EMAIL)).willReturn(List.of(new Role("USER")));
        given(authenticationService.roles(OTHER_EMAIL)).willReturn(List.of(new Role("USER")));
        given(authenticationService.roles(ADMIN_EMAIL))
                .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
    }

    @Test
    void lists() throws Exception {
        mockMvc.perform(
                get("/users")
        )
                .andDo(print())
                .andExpect(content().string(containsString("\"id\":" + EXISTED_ID)))
                .andExpect(content().string(containsString("\"id\":" + CREATED_ID)))
                .andExpect(status().isOk())
                .andDo(document("get-users",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                            fieldWithPath("[].id").type(NUMBER).description("사용자 식별자"),
                            fieldWithPath("[].name").type(STRING).description("사용자 이름"),
                            fieldWithPath("[].email").type(STRING).description("사용자 이메일"),
                            fieldWithPath("[].password").type(STRING).description("사용자 비밀번호"),
                            fieldWithPath("[].deleted").type(BOOLEAN).description("사용자 삭제 여부")
                    )
        ));

        verify(userService).getUsers();
    }

    @Test
    void detailWithExistedId() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/users/{id}", EXISTED_ID )
        )
                .andDo(print())
                .andExpect(content().string(containsString("{\"id\":" + EXISTED_ID)))
                .andExpect(status().isOk())
                .andDo(document("get-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("조회하고자 하는 사용자 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("사용자 식별자"),
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                                fieldWithPath("deleted").type(BOOLEAN).description("사용자 삭제 여부")
                        )
                ));

        verify(userService).getUser(EXISTED_ID);
    }

    @Test
    void detailWithNotExistedId() throws Exception {
        mockMvc.perform(
                get("/users/"+NOT_EXISTED_ID)
        )
                .andExpect(content().string(containsString("User not found")))
                .andExpect(status().isNotFound());

        verify(userService).getUser(NOT_EXISTED_ID);
    }

    @Test
    void createWithValidUser() throws Exception {
        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"createdName\",\"email\":\"createdEmail\",\"password\":\"createdPassword\"}")
        )
                .andExpect(jsonPath("id").value(createdUserData.getId()))
                .andExpect(jsonPath("name").value(createdUserData.getName()))
                .andExpect(jsonPath("email").value(createdUserData.getEmail()))
                .andExpect(jsonPath("password").value(createdUserData.getPassword()))
                .andExpect(status().isCreated())
                .andDo(document("create-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("사용자 식별자"),
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                                fieldWithPath("deleted").type(BOOLEAN).description("사용자 삭제 여부")
                        )
                ));

        verify(userService).createUser(any(UserCreateData.class));
    }

    @Test
    void createWithEmptyUser() throws Exception {
        mockMvc.perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithExistedIdAndUserAndEmail() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/users/{id}", EXISTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + MY_TOKEN)
                        .content("{\"name\":\"updatedName\",\"password\":\"updatedPassword\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("name").value(updatedUserData.getName()))
                .andExpect(jsonPath("password").value(updatedUserData.getPassword()))
                .andExpect(status().isOk())
                .andDo(document("update-user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("수정하고자 하는 사용자의 식별자")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("사용자 식별자"),
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
                                fieldWithPath("deleted").type(BOOLEAN).description("사용자 삭제 여부")
                        )
                ));

        verify(userService).updateUser(eq(EXISTED_ID), any(UserUpdateData.class), eq(validAuthentication));
    }

    @Test
    void updateWithNotExistedId() throws Exception {
        mockMvc.perform(
                patch("/users/" + NOT_EXISTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + MY_TOKEN)
                        .content("{\"name\":\"createdUser\",\"password\":\"createdPassword\"}")
        )
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(NOT_EXISTED_ID), any(UserUpdateData.class), eq(validAuthentication));
    }

    @Test
    void updateWithEmpty() throws Exception {
        mockMvc.perform(
                patch("/users/" + EXISTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + MY_TOKEN)
                        .content("{}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("권한이 없다는 예외를 던지고 UNAUTHORIZED를 리턴한다")
    void updateWithoutToken() throws Exception {
        mockMvc.perform(
                patch("/users/" + EXISTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"createdUser\",\"password\":\"createdPassword\"}")
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("접근이 거부되었다는 예외를 던지고 FORBIDDEN를 리턴한다")
    void updateWithOtherToken() throws Exception {
        mockMvc.perform(
                patch("/users/" + EXISTED_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + OTHER_TOKEN)
                        .content("{\"name\":\"createdUser\",\"password\":\"createdPassword\"}")
        )
                .andDo(print())
                .andExpect(status().isForbidden());

        verify(userService).updateUser(eq(EXISTED_ID), any(UserUpdateData.class), eq(invalidAuthentication));
    }

    @Test
    void deleteWithExistedId() throws Exception {
        mockMvc.perform(
                delete("/users/" + EXISTED_ID)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andDo(print())
                .andExpect(content().string(containsString("\"deleted\":true")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithNotExistedId() throws Exception {
        mockMvc.perform(
                delete("/users/" + NOT_EXISTED_ID)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(NOT_EXISTED_ID);
    }

    @Test
    void deleteWithDeletedId() throws Exception {
        mockMvc.perform(
                delete("/users/" + DELETE_ID)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(DELETE_ID);
    }

    @Test
    void deleteWithoutToken() throws Exception {
        mockMvc.perform(
                delete("/users/" + EXISTED_ID)
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteWithoutAdminRole() throws Exception {
        mockMvc.perform(
                delete("/users/" + EXISTED_ID)
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
