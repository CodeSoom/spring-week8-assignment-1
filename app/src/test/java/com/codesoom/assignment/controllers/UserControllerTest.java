package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {
    private static final String MY_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String OTHER_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjJ9.TEM6MULsZeqkBbUKziCR4Dg_8kymmZkyxsCXlfNJ3g0";
    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjEwMDR9.3GV5ZH3flBf0cnaXQCNNZlT4mgyFyBUhn3LKzQohh1A";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(userService.registerUser(any(UserRegistrationData.class)))
                .will(invocation -> {
                    UserRegistrationData registrationData =
                            invocation.getArgument(0);
                    return User.builder()
                            .id(13L)
                            .email(registrationData.getEmail())
                            .name(registrationData.getName())
                            .build();
                });

        given(
                userService.updateUser(
                        eq(1L),
                        any(UserModificationData.class),
                        eq(1L)
                )
        )
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    UserModificationData modificationData =
                            invocation.getArgument(1);
                    return User.builder()
                            .id(id)
                            .email("tester@example.com")
                            .name(modificationData.getName())
                            .build();
                });

        given(
                userService.updateUser(
                        eq(100L),
                        any(UserModificationData.class),
                        eq(1L)
                )
        )
                .willThrow(new UserNotFoundException(100L));

        given(
                userService.updateUser(
                        eq(1L),
                        any(UserModificationData.class),
                        eq(2L)
                )
        )
                .willThrow(new AccessDeniedException("Access denied"));

        given(userService.deleteUser(100L))
                .willThrow(new UserNotFoundException(100L));

        given(authenticationService.authenticate(MY_TOKEN)).willReturn(
                new UserAuthentication(1L, List.of(new Role("USER"))));

        given(authenticationService.authenticate(OTHER_TOKEN)).willReturn(
                new UserAuthentication(2L, List.of(new Role("USER"))));

        given(authenticationService.authenticate(ADMIN_TOKEN)).willReturn(
                new UserAuthentication(1004L, List.of(new Role("USER"), new Role("ADMIN"))));
    }

    @Test
    @DisplayName("create 메서드는 user 를 생성하여 저장하고 리턴한다")
    void registerUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"tester@example.com\"," +
                                        "\"name\":\"Tester\",\"password\":\"test\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"id\":13")
                ))
                .andExpect(content().string(
                        containsString("\"email\":\"tester@example.com\"")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"Tester\"")
                ))
                .andDo(
                        document("post-users",
                                requestFields(
                                        fieldWithPath("email").description("유저 email"),
                                        fieldWithPath("name").description("유저 name"),
                                        fieldWithPath("password").description("유저 password")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("유저 id"),
                                        fieldWithPath("email").description("유저 email"),
                                        fieldWithPath("name").description("유저 name")
                                )
                        )
                );

        verify(userService).registerUser(any(UserRegistrationData.class));
    }

    @Test
    @DisplayName("create 메서드는 유효하지 않은 attribute 로 요청한 경우 400을 리턴한다")
    void registerUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update 메서드는 요청한 user id 의 user 를 수정하고 리턴한다")
    void updateUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/users/{userId}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"TEST\",\"password\":\"test\"}")
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"TEST\"")
                ))
                .andDo(
                        document("patch-users",
                                requestHeaders(
                                        headerWithName("Authorization").description("수정할 userId와 동일한 token")
                                ),
                                pathParameters(
                                        parameterWithName("userId").description("User Id")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("수정할 유저 name"),
                                        fieldWithPath("password").description("수정할 유저 password")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("유저 id"),
                                        fieldWithPath("email").description("유저 email"),
                                        fieldWithPath("name").description("유저 name")
                                )
                        )
                );

        verify(userService)
                .updateUser(eq(1L), any(UserModificationData.class), eq(1L));
    }

    @Test
    @DisplayName("update 메서드는 유효하지 않은 attribute 로 요청한 경우 400을 리턴한다")
    void updateUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\",\"password\":\"\"}")
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update 메서드는 저장되지 않은 user Id로 요청한 경우 404를 리턴한다")
    void updateUserWithNotExsitedId() throws Exception {
        mockMvc.perform(
                        patch("/users/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"TEST\",\"password\":\"TEST\"}")
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                .andExpect(status().isNotFound());

        verify(userService).updateUser(
                eq(100L),
                any(UserModificationData.class),
                eq(1L));
    }

    @Test
    @DisplayName("update 메서드는 Access Token 이 없이 요청한 경우 401을 리턴한다")
    void updateUserWithoutAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"TEST\",\"password\":\"test\"}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("update 메서드는 다른 user 의 AccessToken 으로 요청한 경우 403을 리턴한다")
    void updateUserWithOthersAccessToken() throws Exception {
        mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"TEST\",\"password\":\"test\"}")
                                .header("Authorization", "Bearer " + OTHER_TOKEN)
                )
                .andExpect(status().isForbidden());

        verify(userService)
                .updateUser(eq(1L), any(UserModificationData.class), eq(2L));
    }

    @Test
    @DisplayName("destroy 메서드는 요청한 user Id의 user 를 삭제한다")
    void destroyWithExistedId() throws Exception {
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/users/{userId}", 1)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(
                        document("delete-users",
                                requestHeaders(
                                        headerWithName("Authorization").description("ADMIN 권한을 갖고 있는 token")
                                ),
                                pathParameters(
                                        parameterWithName("userId").description("User Id")
                                )
                        )
                );

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("destroy 메서드는 요청한 user Id 의 user 가 저장되어 있지 않은 경우 404를 리턴한다")
    void destroyWithNotExistedId() throws Exception {
        mockMvc.perform(
                        delete("/users/100")
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(100L);
    }

    @Test
    @DisplayName("destroy 메서드는 Access Token 이 없이 요청한 경우 401을 리턴한다")
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("destroy 메서드는 ADMIN Role 이 없는 AccessToken 으로 요청한 경우 403을 리턴한다")
    void destroyWithoutAdminRole() throws Exception {
        mockMvc.perform(
                        delete("/users/1")
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                .andExpect(status().isForbidden());
    }
}
