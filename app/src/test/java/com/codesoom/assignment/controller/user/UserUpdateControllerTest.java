package com.codesoom.assignment.controller.user;

import com.codesoom.assignment.application.role.RoleRegisterInterface;
import com.codesoom.assignment.application.user.UserRegisterInterface;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.util.JwtUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserUpdateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRegisterInterface userRegisterService;
    @Autowired
    private RoleRegisterInterface roleRegisterService;
    @Autowired
    private JwtUtil jwtUtil;

    private UserRegistrationData newUserRegistrationData(String text){
        return new UserRegistrationData(text , text ,text);
    }

    private UserModificationData newUserModificationData(String text){
        return new UserModificationData("modifier" , "modifier");
    }

    @Test
    @DisplayName("USER 권한을 가지고 사용자와 수정자가 같고 사용자 수정 DTO 빈 검증을 통과한다면 사용자를 수정하고 수정된 정보와 정상 처리 응답 상태를 반환한다.")
    @Transactional
    void If_UserRole_RegisterEqualsModifier_PassedValidation_UpdateUser() throws Exception {
        // given
        final UserRegistrationData registrationData = newUserRegistrationData("register");
        final User registeredUser = userRegisterService.registerUser(registrationData);
        final Role registeredRole = roleRegisterService.save(new Role(registeredUser.getId() , "USER"));
        final String USER_TOKEN = jwtUtil.encode(registeredUser.getId());

        final String modificationContent = objectMapper.writeValueAsString(newUserModificationData("modifier"));
        // when
        final ResultActions result = mockMvc.perform(
                        patch("/users/" + registeredUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(modificationContent)
                                .header("Authorization" , "Bearer " + USER_TOKEN)
                )
                .andDo(print())
                .andDo(document("PATCH /users/{id}"));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(registeredUser.getId()))
                .andExpect(jsonPath("$.name").value("modifier"))
        ;
    }
}
