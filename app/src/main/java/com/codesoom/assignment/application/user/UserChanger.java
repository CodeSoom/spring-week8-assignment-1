package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.user.UserModificationDto;

public interface UserChanger {
    void deleteById(Long id);
    User save(Long id, UserModificationDto modificationDto);
}
