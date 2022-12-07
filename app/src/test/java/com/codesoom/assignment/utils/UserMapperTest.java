package com.codesoom.assignment.utils;


import com.codesoom.assignment.adapter.in.web.user.dto.UserMapper;
import com.codesoom.assignment.adapter.in.web.user.dto.request.UserCreateRequestDto;
import com.codesoom.assignment.adapter.in.web.user.dto.request.UserUpdateRequestDto;
import com.codesoom.assignment.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.codesoom.assignment.support.UserFixture.회원_1번;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapstruct를 사용한 User 객체 매핑 테스트")
class UserMapperTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Create_Reqeust_Dto_To_Entity_메서드는 {

        @Test
        @DisplayName("RequestDto 객체의 데이터를 Entity 객체로 매핑한다")
        void it_returns_entity() {
            UserCreateRequestDto userCreateRequestDto = 회원_1번.등록_요청_데이터_생성();

            User user = UserMapper.INSTANCE.toEntity(userCreateRequestDto);

            assertThat(user).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getName()).isEqualTo(회원_1번.이름());
            assertThat(user.getEmail()).isEqualTo(회원_1번.이메일());
            assertThat(user.getPassword()).isEqualTo(회원_1번.비밀번호());
            assertThat(user.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Update_Reqeust_Dto_To_Entity_메서드는 {

        @Test
        @DisplayName("RequestDto 객체의 데이터를 Entity 객체로 매핑한다")
        void it_returns_entity() {
            UserUpdateRequestDto userUpdateRequestDto = 회원_1번.수정_요청_데이터_생성();

            User user = UserMapper.INSTANCE.toEntity(userUpdateRequestDto);

            assertThat(user).isNotNull();
            assertThat(user.getId()).isNull();
            assertThat(user.getName()).isEqualTo(회원_1번.이름());
            assertThat(user.getEmail()).isNull();
            assertThat(user.getPassword()).isEqualTo(회원_1번.비밀번호());
            assertThat(user.isDeleted()).isFalse();
        }
    }
}
