package com.codesoom.assignment.application.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserModificationData;
import com.codesoom.assignment.error.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateService implements UserUpdateInterface {

    private final UserFindInterface userFindService;
    private final Mapper mapper;

    public UserUpdateService(UserFindInterface userFindService, Mapper mapper) {
        this.userFindService = userFindService;
        this.mapper = mapper;
    }

    /**
     * 사용자를 수정한다.
     *
     * @param id 수정할 사용자의 식별자
     * @param modificationData 수정할 사용자의 정보
     * @throws AccessDeniedException 사용자 자원의 식별자와 수정자의 식별자가 다를 경우
     * @throws UserNotFoundException 식별자에 해당하는 사용자가 존재하지 않을 경우
     * @return 수정된 사용자
     */
    @Override
    public User updateUser(Long id, UserModificationData modificationData) throws AccessDeniedException {
        if (modificationData.isDifferentUser(id)) {
            throw new AccessDeniedException("Access denied");
        }

        User user = userFindService.findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }
}
