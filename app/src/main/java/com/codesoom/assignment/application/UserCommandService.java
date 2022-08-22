package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.UserInquiryInfo;
import com.codesoom.assignment.dto.UserRegisterData;

public interface UserCommandService {
    /**
     * 유저 등록 정보를 받아 유저를 생성하고 유저 조회 정보를 리턴합니다.
     *
     * @param registerData 유저 등록 정보
     * @return 유저 조회 정보
     */
    UserInquiryInfo register(UserRegisterData registerData);

    /**
     * 유저 식별자를 받아 유저를 삭제합니다.
     *
     * @param id Long 식별자
     */
    void delete(Long id);
}
