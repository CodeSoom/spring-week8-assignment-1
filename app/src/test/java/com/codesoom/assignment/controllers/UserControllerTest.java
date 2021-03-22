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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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

    private static final Long MY_ID = 1L;
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
    private Long givenValidId;
    private Long givenInvalidId;
    private Long givenOtherId;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        updatedUser = User.builder()
                .name(NAME)
                .email(UPDATE_EMAIL)
                .password(UPDATE_PASSWORD)
                .build();

        given(userService.createUser(any(UserCreateRequestDto.class)))
                .willReturn(user);

        given(userService.updateUser(eq(MY_ID), any(UserUpdateRequestDto.class), eq(MY_ID)))
                .willReturn(updatedUser);

        given(userService.updateUser(eq(NOT_EXIST_ID), any(UserUpdateRequestDto.class), eq(MY_ID)))
                .willThrow(new UserNotFoundException(NOT_EXIST_ID));

        given(userService.updateUser(eq(OTHER_ID), any(UserUpdateRequestDto.class), eq(MY_ID)))
                .willThrow(new AccessDeniedException("Access denied"));

        given(userService.deleteUser(eq(NOT_EXIST_ID)))
                .willThrow(new UserNotFoundException(NOT_EXIST_ID));

        given(authenticationService.parseToken(MY_TOKEN))
                .willReturn(MY_ID);

        given(authenticationService.parseToken(OTHER_TOKEN))
                .willReturn(OTHER_ID);

        given(authenticationService.parseToken(ADMIN_TOKEN))
                .willReturn(ADMIN_ID);

        given(authenticationService.roles(MY_ID))
                .willReturn(Arrays.asList(new Role("USER")));

        given(authenticationService.roles(OTHER_ID))
                .willReturn(Arrays.asList(new Role("USER")));

        given(authenticationService.roles(ADMIN_ID))
                .willReturn(Arrays.asList(new Role("ADMIN")));
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        UserCreateRequestDto invalidAttributes;

        @Nested
        @DisplayName("사용자 정보가 주어진다면")
        class Context_with_valid_attributes {

            @BeforeEach
            void setUp() {
                createRequest = UserCreateRequestDto.builder()
                        .name(NAME)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("새로운 사용자를 등록하고 사용자와 응답코드 201을 반환한다")
            void it_returns_user_and_created() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest))
                )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("name").value(NAME))
                        .andExpect(jsonPath("email").value(EMAIL))
                        .andDo(document("create-user",
                                requestFields(
                                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                                        fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                        fieldWithPath("password").type(STRING).description("사용자 비밀번호")),
                                responseFields(
                                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                                        fieldWithPath("email").type(STRING).description("사용자 이메일"),
                                        fieldWithPath("password").type(STRING).description("사용자 비밀번호"))
                        ));


                verify(userService).createUser(any(UserCreateRequestDto.class));
            }
        }

        @Nested
        @DisplayName("사용자 정보가 주어지지 않는다면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                invalidAttributes = UserCreateRequestDto.builder()
                        .name("")
                        .email("")
                        .password("")
                        .build();
            }

            @Test
            @DisplayName("응답코드 400을 반환한다")
            void it_returns_bad_request() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        Long userId;
        UserUpdateRequestDto invalidAttributes;

        @Nested
        @DisplayName("등록된 사용자 ID와 수정할 정보가 주어진다면")
        class Context_with_existed_id_and_valid_attributes {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                givenValidId = MY_ID;

                userId = MY_ID;
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자의 정보를 수정하고 사용자와 응답코드 200을 반환한다")
            void it_returns_user_and_ok() throws Exception {
                mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("email").value(UPDATE_EMAIL));

                verify(userService).updateUser(eq(givenValidId), any(UserUpdateRequestDto.class), eq(userId));
            }
        }


        @Nested
        @DisplayName("등록되지 않은 사용자 ID와 수정할 정보가 주어진다면")
        class Context_with_not_existed_id_and_valid_attributes {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                givenInvalidId = NOT_EXIST_ID;

                userId = MY_ID;
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(
                        patch("/users/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                        .andExpect(status().isNotFound());

                verify(userService).updateUser(eq(givenInvalidId), any(UserUpdateRequestDto.class), eq(userId));
            }
        }

        @Nested
        @DisplayName("수정할 사용자 정보가 주어지지 않는다면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                invalidAttributes = UserUpdateRequestDto.builder()
                        .email("")
                        .password("")
                        .build();
            }

            @Test
            @DisplayName("응답코드 400을 반환한다")
            void it_returns_bad_request() throws Exception {
                mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidAttributes))
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("권한이 없는 ID와 수정할 정보가 주어진다면")
        class Context_with_other_id_and_valid_attributes {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();

                givenOtherId = OTHER_ID;

                userId = MY_ID;
            }

            @Test
            @DisplayName("응답코드 403를 반환한다")
            void it_returns_forbidden() throws Exception {
                mockMvc.perform(
                        patch("/users/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                                .header("Authorization", "Bearer " + MY_TOKEN)
                )
                        .andExpect(status().isForbidden());

                verify(userService).updateUser(eq(givenOtherId), any(UserUpdateRequestDto.class), eq(userId));
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않는다면")
        class Context_without_access_token {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("응답코드 401을 반환한다")
            void it_returns_unauthorized() throws Exception {
                mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isUnauthorized());
            }
        }

        @DisplayName("타인의 토큰으로 접근한다면")
        class Context_with_others_access_token {

            @BeforeEach
            void setUp() {
                updateRequest = UserUpdateRequestDto.builder()
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("응답코드 403을 반환한다")
            void it_returns_forbidden() throws Exception {
                mockMvc.perform(
                        patch("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                                .header("Authorization", "Bearer " + OTHER_TOKEN)

                )
                        .andExpect(status().isForbidden());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("삭제할 사용자 ID가 주어진다면")
        class Context_with_existed_id {

            @BeforeEach
            void setUp() {
                givenValidId = MY_ID;
            }

            @Test
            @DisplayName("해당 ID를 갖는 사용자를 삭제하고 응답코드 200을 반환한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                        .andExpect(status().isOk());

                verify(userService).deleteUser(givenValidId);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자 ID가 주어진다면")
        class Context_with_not_existed_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_not_found() throws Exception {
                mockMvc.perform(delete("/users/100")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                        .andExpect(status().isNotFound());

                verify(userService).deleteUser(givenInvalidId);
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않는다면")
       class Context_without_access_token {

            @Test
            @DisplayName("응답코드 401을 반환한다")
            void it_returns_ok() throws Exception {
                mockMvc.perform(delete("/users/1"))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("관리자 토큰이 주어지지 않는다면")
        class Context_without_admin_role {

            @Test
            @DisplayName("응답코드 401를 반환한다")
            void it_returns_forbidden() throws Exception {
                mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + MY_TOKEN)
                )
                        .andExpect(status().isForbidden());
            }
        }
    }
}
