package com.codesoom.assignment.application;

import com.codesoom.assignment.Fixture;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserInquiryInfo;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserQueryServiceTest {
    @Autowired
    private UserCommandService userCommandService;
    @Autowired
    private UserQueryService userQueryService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("findById 메서드는 식별자가 주어지면 유저를 리턴한다.")
    void findByIdReturnsUserInfoGivenId() {
        // given
        final User givenUser = userCommandService.register(Fixture.USER_REGISTER_DATA);

        // when
        final User user = userQueryService.findById(givenUser.getId());

        // then
        assertThat(user.getId()).isEqualTo(givenUser.getId());
    }
}
