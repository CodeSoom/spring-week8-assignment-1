package com.codesoom.assignment.controller.user;

import com.codesoom.assignment.dto.UserRegistrationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 등록 DTO 빈 검증을 통과한다면 사용자를 저장하고 사용자를 생성했다는 응답 상태를 반환한다.")
    @Transactional
    void If_ValidationPath_CreateUser() throws Exception {
        // given
        final UserRegistrationData registrationData = new UserRegistrationData("register" , "register" , "register");
        final String registrationContent = objectMapper.writeValueAsString(registrationData);;

        // when
        final ResultActions result = mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registrationContent)
                )
                .andDo(print())
                .andDo(document("POST /users"));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("register"))
                .andExpect(jsonPath("$.name").value("register"))
        ;
    }
}
