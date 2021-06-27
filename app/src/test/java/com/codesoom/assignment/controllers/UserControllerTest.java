package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.codesoom.assignment.ApiDocumentUtils.getDocumentRequest;
import static com.codesoom.assignment.ApiDocumentUtils.getDocumentResponse;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
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

        given(authenticationService.parseToken(MY_TOKEN)).willReturn(1L);
        given(authenticationService.parseToken(OTHER_TOKEN)).willReturn(2L);
        given(authenticationService.parseToken(ADMIN_TOKEN)).willReturn(1004L);

        given(authenticationService.roles(1L))
                .willReturn(Arrays.asList(new Role("USER")));
        given(authenticationService.roles(2L))
                .willReturn(Arrays.asList(new Role("USER")));
        given(authenticationService.roles(1004L))
                .willReturn(Arrays.asList(new Role("USER"), new Role("ADMIN")));
    }

    @Test
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
                .andDo(document("users-post",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                             ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("식별자"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                                              )));

        verify(userService).registerUser(any(UserRegistrationData.class));
    }

    @Test
    void registerUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/users/{id}", 1L)
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
                .andDo(document("users-patch",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("id").description("식별자")
                                              ),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                             ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("식별자"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                                              )));

        verify(userService)
                .updateUser(eq(1L), any(UserModificationData.class), eq(1L));
    }

    @Test
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
    void updateUserWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TEST\",\"password\":\"test\"}")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
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
    void destroyWithExistedId() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/users/{id}", 1L)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document("users-delete",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("id").description("식별자")
                                              )));

        verify(userService).deleteUser(1L);
    }

    @Test
    void destroyWithNotExistedId() throws Exception {
        mockMvc.perform(
                delete("/users/100")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(100L);
    }

    @Test
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void destroyWithoutAdminRole() throws Exception {
        mockMvc.perform(
                delete("/users/1")
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isForbidden());
    }
}
