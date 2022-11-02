package com.codesoom.assignment.controllers.restdocs;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.codesoom.assignment.controllers.restdocs.RestDocsUtils.getDocumentRequest;
import static com.codesoom.assignment.controllers.restdocs.RestDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class UserDocumentationTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(provider))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Nested
    @DisplayName("/users [POST] REST Docs")
    class Describe_create {
        // given
        private final UserRegistrationData request = new UserRegistrationData(
                "gilsoon@test.com",
                "홍길순",
                "password1234!"
        );

        @Test
        void create() throws Exception {
            // when
            final ResultActions resultActions = mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(
                            document("users-create",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    requestFields(
                                            fieldWithPath("email").type(STRING).description("이메일"),
                                            fieldWithPath("name").type(STRING).description("이름"),
                                            fieldWithPath("password").type(STRING).description("패스워드")
                                    ),
                                    responseFields(
                                            fieldWithPath("id").type(NUMBER).description("회원ID"),
                                            fieldWithPath("email").type(STRING).description("이메일"),
                                            fieldWithPath("name").type(STRING).description("이름")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("/users/{id} [PATCH] REST Docs")
    class Describe_update {
        private final UserRegistrationData param = new UserRegistrationData(
                "gildong@test.com",
                "홍길동",
                "test1234!"
        );
        private final User savedUser = userService.registerUser(param);
        private final String VALID_TOKEN = jwtUtil.encode(savedUser.getId());


        @Test
        void update() throws Exception {
            // given
            final UserModificationData request = new UserModificationData(
                    "홍길돈",
                    "test4321!"
            );

            // when
            final ResultActions resultActions = mockMvc.perform(patch("/users/{id}", savedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", "Bearer " + VALID_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(
                            document("users-update",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("id").description("회원ID")
                                    ),
                                    requestFields(
                                            fieldWithPath("name").type(STRING).description("이름"),
                                            fieldWithPath("password").type(STRING).description("패스워드")
                                    ),
                                    responseFields(
                                            fieldWithPath("id").type(NUMBER).description("회원ID"),
                                            fieldWithPath("email").type(STRING).description("이메일"),
                                            fieldWithPath("name").type(STRING).description("이름")
                                    )
                            )
                    );
        }
    }

    @Nested
    @DisplayName("/users/{id} [DELETE] REST Docs")
    class Describe_destroy {
        private final UserRegistrationData param = new UserRegistrationData(
                "codesoom@test.com",
                "코드숨",
                "code1234!"
        );

        private final User savedUser = userService.registerUser(param);
        private final String VALID_TOKEN = jwtUtil.encode(savedUser.getId());

        @Test
        void destroy() throws Exception {
            // when
            final ResultActions resultActions = mockMvc.perform(delete("/users/{id}", savedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + VALID_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(
                            document("users-delete",
                                    getDocumentRequest(),
                                    getDocumentResponse(),
                                    pathParameters(
                                            parameterWithName("id").description("회원ID")
                                    )
                            )
                    );
        }
    }

}
