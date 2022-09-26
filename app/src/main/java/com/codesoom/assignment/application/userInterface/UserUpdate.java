package com.codesoom.assignment.application.userInterface;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserModificationData;

public interface UserUpdate {
    /**
     * 사용자를 수정한다.
     *
     * @param id 수정할 사용자의 식별자
     * @param modificationData 수정할 사용자의 정보
     * @return 수정된 사용자
     */
    User updateUser(Long id , UserModificationData modificationData , Long userId);
}
