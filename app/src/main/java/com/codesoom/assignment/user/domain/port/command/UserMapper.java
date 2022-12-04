package com.codesoom.assignment.user.domain.port.command;

import com.codesoom.assignment.user.repository.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, // 빌드 시 구현체 생성 후 빈으로 등록합니다.
        injectionStrategy = InjectionStrategy.CONSTRUCTOR) // 생성자 주입 전략을 따릅니다.
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * 회원 생성 RequestDto에서 회원 엔티티로 객체를 매핑합니다.
     *
     * @param userCreateRequest 등록할 회원 정보
     * @return 데이터가 매핑된 회원 엔티티
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toEntity(UserCreateRequest userCreateRequest);

    /**
     * 회원 수정 RequestDto에서 회원 엔티티로 객체를 매핑합니다.
     *
     * @param userUpdateRequest 수정할 회원 정보
     * @return 데이터가 매핑된 회원 엔티티
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toEntity(UserUpdateRequest userUpdateRequest);
}
