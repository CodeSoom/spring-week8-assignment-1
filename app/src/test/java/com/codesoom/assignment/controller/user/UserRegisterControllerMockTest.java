package com.codesoom.assignment.controller.user;

import com.codesoom.assignment.application.user.UserRegisterInterface;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRegisterControllerMockTest {

    private MockMvc mockMvc;

    private UserRegisterController userRegisterController;
    private UserRegisterInterface userRegisterService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userRegisterService = mock(UserRegisterInterface.class);
        userRegisterController = new UserRegisterController(userRegisterService);

        mockMvc = MockMvcBuilders
                    .standaloneSetup(userRegisterController)
                    .build();
    }

    @Nested
    @DisplayName("create()")
    class Describe_Create{

        @Nested
        @DisplayName("Service에서 null을 반환한다면")
        class Context_ServiceNullReturn{

            final UserRegistrationData registrationData = new UserRegistrationData("register" , "register" , "register");
            String registrationContent;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                given(userRegisterService.registerUser(any(UserRegistrationData.class)))
                        .willReturn(null);

                registrationContent = objectMapper.writeValueAsString(registrationData);
            }

            @Test
            @DisplayName("자원을 생성했다는 상태와 바디는 공백으로 반환된다.")
            void It_CreatedStatusAndBlankBody() throws Exception {
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registrationContent))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(content().string(matches("")));
            }
        }
    }
}
