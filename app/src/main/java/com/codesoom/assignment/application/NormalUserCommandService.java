package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegisterRequest;
import com.codesoom.assignment.errors.DuplicatedEmailException;
import org.springframework.stereotype.Service;

@Service
class NormalUserCommandService implements UserCommandService {
    private final UserRepository userRepository;

    public NormalUserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedEmailException(request.getEmail());
        }

        return userRepository.save(new User(request));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
