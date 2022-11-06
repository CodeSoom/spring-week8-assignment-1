package com.codesoom.assignment.support.test;

import com.codesoom.assignment.support.FieldsItem;
import com.codesoom.assignment.support.RestDocsControllerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.codesoom.assignment.support.FieldsItem.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("FieldsItem 테스트")
public class FieldsItemTest extends RestDocsControllerSupport {

    FieldsItem items =
            of("member", OBJECT, "회원",
                of("id", NUMBER, "회원 아이디"),
                of("name", STRING, "회원명"),
                of("favorite", OBJECT, "좋아하는 것",
                    of("music", OBJECT, "음악",
                        of("artist", STRING, "가수"),
                        of("title", STRING, "곡명")),
                    of("movie", OBJECT, "영화",
                        of("name", STRING, "영화명"),
                        of("star", NUMBER, "평점"))));

    @Test
    void 재귀문_구현_테스트() throws Exception {
        ResultActions result = mockMvc.perform(get("/test")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(responseFields(items.recursiveBuild())));
    }

    @Test
    void 반복문_구현_테스트() throws Exception {
        ResultActions result = mockMvc.perform(get("/test")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(responseFields(items.iterableBuild())));
    }
}
