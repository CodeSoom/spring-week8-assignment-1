package com.codesoom.assignment.controllers;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController 클래스의")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private Map<String, String> postRequest(Map<String, String> data, String path) throws Exception {
        return objectMapper.readValue(mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<Map<String, String>>() {
        });
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("register 메서드는")
    class Describe_register {
        @Nested
        @DisplayName("유저 정보가 주어지면")
        class Context_userData {
            @Test
            @DisplayName("유저와 201을 응답한다")
            void It_returns_user() throws Exception {
                mockMvc.perform(post(Fixture.USER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Fixture.USER_DATA_MAP)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.email", Is.is(Fixture.EMAIL)))
                        .andExpect(jsonPath("$.name", Is.is(Fixture.USER_NAME)))
                        .andExpect(jsonPath("$.role", Is.is(Fixture.ROLE_USER)));
            }
        }

        @Nested
        @DisplayName("중복되는 이메일이 주어지면")
        class Context_with_email {
            @BeforeEach
            void prepare() throws Exception {
                postRequest(Fixture.USER_DATA_MAP, Fixture.USER_PATH);
            }

            @Test
            @DisplayName("예외 메시지와 400을 응답한다")
            void it_returns_exception() throws Exception {
                mockMvc.perform(post(Fixture.USER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Fixture.USER_DATA_MAP)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").isString());
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("관리자 권한을 가진 토큰과 식별자가 주어지면")
        class Context_with_adminToken {
            private String userIdToDelete;
            private String adminToken;

            @BeforeEach
            void prepare() throws Exception {
                userIdToDelete = postRequest(Fixture.USER_DATA_MAP, Fixture.USER_PATH).get("id");

                User admin = Fixture.ADMIN;
                admin.giveAdminPrivileges();
                userRepository.save(admin);

                adminToken = postRequest(Fixture.ADMIN_LOGIN_DATA_MAP, Fixture.SESSION_PATH)
                        .get("accessToken");
            }

            @Test
            @DisplayName("유저를 삭제하고 204를 응답한다")
            void It_respond_noContent() throws Exception {
                mockMvc.perform(delete(Fixture.USER_PATH + "/" + userIdToDelete)
                                .header("Authorization", "Bearer " + adminToken))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("유저 권한을 가진 토큰과 식별자가 주어지면")
        class Context_with_userToken {
            private String userIdToDelete;
            private String userToken;

            @BeforeEach
            void prepare() throws Exception {
                userIdToDelete = postRequest(Fixture.USER_DATA_MAP, Fixture.USER_PATH).get("id");

                userToken = postRequest(Fixture.LOGIN_DATA_MAP, Fixture.SESSION_PATH)
                        .get("accessToken");
            }

            @Test
            @DisplayName("예외를 응답한다")
            void It_respond_exception() throws Exception {
                mockMvc.perform(delete(Fixture.USER_PATH + "/" + userIdToDelete)
                                .header("Authorization", "Bearer " + userToken))
                        .andExpect(status().isForbidden());
            }
        }
    }
}
