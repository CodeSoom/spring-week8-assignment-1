package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.user.UserDeleteInterface;
import com.codesoom.assignment.application.user.UserDeleteService;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDeleteInterface userDeleteService;

    @Nested
    @DisplayName("create()")
    class Describe_Create{

        @Nested
        @DisplayName("사용자 등록 DTO 빈 검증을 통과한다면")
        class Context_PassedBeanValidation{

            private final UserRegistrationData registrationData = new UserRegistrationData("register" , "register" , "register");
            private String registrationContent;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                registrationContent = objectMapper.writeValueAsString(registrationData);
            }

            @Test
            @DisplayName("사용자를 저장하고 사용자를 생성했다는 응답 상태를 반환한다.")
            void It_CreateUser() throws Exception {

                mockMvc.perform(
                            post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registrationContent)
                        )
                        .andDo(print())
                        .andDo(document("POST /users"))
                        .andExpect(status().isCreated());
            }
        }
    }
}
