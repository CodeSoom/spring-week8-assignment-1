package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserInquiryInfo;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
class NormalUserQueryService implements UserQueryService {
    private final UserRepository userRepository;

    public NormalUserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInquiryInfo findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return UserInquiryInfo.from(user);
    }
}