package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserInquiryInfo;
import com.codesoom.assignment.dto.UserRegisterData;
import com.codesoom.assignment.errors.DuplicatedEmailException;
import org.springframework.stereotype.Service;

@Service
class NormalUserCommandService implements UserCommandService {
    private final UserRepository userRepository;

    public NormalUserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserInquiryInfo register(UserRegisterData registerData) {
        if (userRepository.existsByEmail(registerData.getEmail())) {
            throw new DuplicatedEmailException(registerData.getEmail());
        }

        User user = userRepository.save(registerData.toUser());
        return UserInquiryInfo.from(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
