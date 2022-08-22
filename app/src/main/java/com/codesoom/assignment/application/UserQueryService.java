package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.UserInquiryInfo;

public interface UserQueryService {
    /**
     * 유저를 찾아 유저 정보를 리턴한다.
     *
     * @param id 식별자
     * @return 유저 정보
     */
    UserInquiryInfo findById(Long id);
}
