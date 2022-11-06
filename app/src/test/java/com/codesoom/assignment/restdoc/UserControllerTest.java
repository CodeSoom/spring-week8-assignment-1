package com.codesoom.assignment.restdoc;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.support.RestDocsControllerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.codesoom.assignment.support.FieldsItem.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("회원 컨트롤러 테스트")
public class UserControllerTest extends RestDocsControllerSupport {

    @Test
    void 회원_등록() throws Exception {
        given(userService.registerUser(any(UserRegistrationData.class))).willReturn(getUser());

        ResultActions result = mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserRegistration())));

        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                of("name", STRING, "회원명").toField(),
                                of("email", STRING, "이메일").toField(),
                                of("password", STRING, "비밀번호").toField()
                        ),
                        responseFields(
                                of("id", NUMBER, "회원 아이디").toField(),
                                of("name", STRING, "회원명").toField(),
                                of("email", STRING, "이메일").toField()
                        )
                ));
    }

    @Test
    void 회원_수정() throws Exception {
        given(userService.updateUser(anyLong(), any(UserModificationData.class), anyLong())).willReturn(getUser());

        ResultActions result = mockMvc.perform(patch("/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserModification())));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 식별 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("수정할 회원 아이디")
                        ),
                        requestFields(
                                of("name", STRING, "회원명").toField(),
                                of("password", STRING, "비밀번호").toField()
                        ),
                        responseFields(
                                of("id", NUMBER, "회원 아이디").toField(),
                                of("name", STRING, "회원명").toField(),
                                of("email", STRING, "이메일").toField()
                        )
                ));
    }

    @Test
    void 회원_삭제() throws Exception {
        given(userService.deleteUser(anyLong())).willReturn(getUser());

        ResultActions result = mockMvc.perform(delete("/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 식별 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("삭제할 회원 아이디")
                        )
                ));
    }

    private UserRegistrationData getUserRegistration() {
        return new UserRegistrationData("tester@example.com", "홍길동", "test12345");
    }

    private UserModificationData getUserModification() {
        return new UserModificationData("홍길동", "test12345");
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("tester@example.com")
                .name("홍길동")
                .password("test12345")
                .build();
    }
}
