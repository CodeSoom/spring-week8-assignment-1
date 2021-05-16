package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserCreateRequestDto;
import com.codesoom.assignment.dto.UserUpdateRequestDto;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    private static final Long USER_ID = 1L;
    private static final Long NOT_EXIST_ID = 100L;
    private static final Long OTHER_ID = 2L;
    private static final Long ADMIN_ID = 500L;

    private static final String MY_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String OTHER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.TEM6MULsZeqkBbUKziCR4Dg_8kymmZkyxsCXlfNJ3g0";
    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjUwMH0.lmL2u2zGGbDD3H7piWdn1HZU1eZFOXo9Tj4wZWgkiPE";

    private static final String NAME = "양효주";
    private static final String EMAIL = "yhyojoo@codesoom.com";
    private static final String PASSWORD = "112233!!";

    private static final String UPDATE_EMAIL = "joo@codesoom.com";
    private static final String UPDATE_PASSWORD = "123!";

    private User user;
    private User updatedUser;
    private UserCreateRequestDto createRequest;
    private UserUpdateRequestDto updateRequest;
    private UserCreateRequestDto invalidCreateAttributes;
    private UserUpdateRequestDto invalidUpdateAttributes;
    private Long givenValidId;
    private Long givenInvalidId;
    private Long givenOtherId;
    private Long userId;

    @BeforeEach
    void setUp() {
        createRequest = UserCreateRequestDto.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        updateRequest = UserUpdateRequestDto.builder()
                .email(UPDATE_EMAIL)
                .password(UPDATE_PASSWORD)
                .build();

        invalidCreateAttributes = UserCreateRequestDto.builder()
                .name("")
                .email("")
                .password("")
                .build();

        invalidUpdateAttributes = UserUpdateRequestDto.builder()
                .email("")
                .password("")
                .build();

        user = User.builder()
                .id(USER_ID)
                .name(NAME)
                .email(EMAIL)
                .build();

        updatedUser = User.builder()
                .id(USER_ID)
                .name(NAME)
                .email(UPDATE_EMAIL)
                .build();

        givenValidId = USER_ID;
        givenInvalidId = NOT_EXIST_ID;
        givenOtherId = OTHER_ID;
        userId = USER_ID;

        given(userService.createUser(any(UserCreateRequestDto.class)))
                .willReturn(user);

        given(userService.updateUser(eq(USER_ID), any(UserUpdateRequestDto.class), eq(USER_ID)))
                .willReturn(updatedUser);

        given(userService.updateUser(eq(NOT_EXIST_ID), any(UserUpdateRequestDto.class), eq(USER_ID)))
                .willThrow(new UserNotFoundException(NOT_EXIST_ID));

        given(userService.updateUser(eq(OTHER_ID), any(UserUpdateRequestDto.class), eq(USER_ID)))
                .willThrow(new AccessDeniedException("Access denied"));

        given(userService.deleteUser(eq(NOT_EXIST_ID)))
                .willThrow(new UserNotFoundException(NOT_EXIST_ID));

        given(authenticationService.parseToken(MY_TOKEN))
                .willReturn(USER_ID);

        given(authenticationService.parseToken(OTHER_TOKEN))
                .willReturn(OTHER_ID);

        given(authenticationService.parseToken(ADMIN_TOKEN))
                .willReturn(ADMIN_ID);

        given(authenticationService.roles(USER_ID))
                .willReturn(Arrays.asList(new Role("USER")));

        given(authenticationService.roles(OTHER_ID))
                .willReturn(Arrays.asList(new Role("USER")));

        given(authenticationService.roles(ADMIN_ID))
                .willReturn(Arrays.asList(new Role("ADMIN")));
    }

    @Test
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.
                post("/users")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(NAME))
                .andExpect(jsonPath("email").value(EMAIL))
                .andDo(document("create-user"));

        verify(userService).createUser(any(UserCreateRequestDto.class));
    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateAttributes))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithExistedUserId() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.
                patch("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(UPDATE_EMAIL))
                .andDo(document("update-user"));

        verify(userService).updateUser(eq(givenValidId), any(UserUpdateRequestDto.class), eq(userId));
    }

    @Test
    void updateWithNotExistedUserId() throws Exception {
        mockMvc.perform(
                patch("/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(givenInvalidId), any(UserUpdateRequestDto.class), eq(userId));
    }

    @Test
    void updateWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateAttributes))
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithOthersId() throws Exception {
        mockMvc.perform(
                patch("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isForbidden());

        verify(userService).updateUser(eq(givenOtherId), any(UserUpdateRequestDto.class), eq(userId));
    }

    @Test
    void updateWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithOthersAccessToken() throws Exception {
        mockMvc.perform(
                patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("Authorization", "Bearer " + OTHER_TOKEN)

        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteWithExistedUserId() throws Exception {
        mockMvc.perform(delete("/users/1")
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isOk());

        verify(userService).deleteUser(USER_ID);
    }

    @Test
    void deleteWithNotExistedUserId() throws Exception {
        mockMvc.perform(delete("/users/100")
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(NOT_EXIST_ID);
    }

    @Test
    void deleteWithoutAccessToken() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteWithoutAdminToken() throws Exception {
        mockMvc.perform(delete("/users/1")
                .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isForbidden());
    }
}
