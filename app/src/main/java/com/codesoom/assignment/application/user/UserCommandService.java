package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.user.UserModificationDto;
import com.codesoom.assignment.dto.user.UserRegistrationDto;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserCommandService implements UserCreator, UserChanger {
    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(UserRegistrationDto userRegistrationDto) {
        User user = User.of(
                userRegistrationDto.getUsername(),
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword()
        );
        return userRepository.save(user);
    }

    @Override
    public User save(Long id, UserModificationDto modificationDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("ID [" + id + "]를 찾지 못했기 떄문에 사용자 업데이트를 실패했습니다."));
        user.change(
                modificationDto.getUsername(),
                modificationDto.getPassword()
        );
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("ID [" + id + "]를 찾지 못했기 때문에 사용자 삭제를 실패했습니다.")
                );
        userRepository.delete(user);
    }
}
