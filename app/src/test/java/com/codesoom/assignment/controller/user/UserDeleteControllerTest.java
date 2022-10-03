package com.codesoom.assignment.controller.user;

import com.codesoom.assignment.application.role.RoleRegisterInterface;
import com.codesoom.assignment.application.user.UserRegisterInterface;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserDeleteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRegisterInterface userRegisterService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RoleRegisterInterface roleRegisterService;

    private UserRegistrationData newUserRegistrationData(String text){
        return UserRegistrationData.builder()
                                    .email(text)
                                    .name(text)
                                    .password(text)
                                    .build();
    }

    @Test
    @DisplayName("관리자 권한이고 식별자에 해당하는 사용자가 존재한다면 해당 사용자의 delete를 true로 바꾸고 정상적으로 처리 됐다는 상태를 반환한다.")
    @Transactional
    void If_AdminRole_ExistedUser_UserDeleteTrue() throws Exception {
        // given
        final User admin = userRegisterService.registerUser(newUserRegistrationData("admin"));
        final User user = userRegisterService.registerUser(newUserRegistrationData("user"));

        roleRegisterService.save(new Role(admin.getId() , "ADMIN"));

        final String ADMIN_TOKEN = jwtUtil.encode(admin.getId());

        // when
        ResultActions resultActions = mockMvc.perform(
                        delete("/users/" + user.getId())
                        .header("Authorization" , "Bearer " + ADMIN_TOKEN)
                )
                .andDo(print())
                .andDo(document("delete-deleteUser"));

        // then
        resultActions.andExpect(status().isNoContent());
    }
}
