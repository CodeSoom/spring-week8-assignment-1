package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.error.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteService implements UserDeleteInterface {

    private final UserFindInterface userFindService;

    public UserDeleteService(UserFindInterface userFindService) {
        this.userFindService = userFindService;
    }

    /**
     * 사용자를 삭제한다.
     *
     * @param id 삭제할 사용자의 식별자
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 삭제된 사용자
     */
    @Override
    public User deleteUser(Long id) {
        User user = userFindService.findUser(id);
        user.destroy();
        return user;
    }
}
