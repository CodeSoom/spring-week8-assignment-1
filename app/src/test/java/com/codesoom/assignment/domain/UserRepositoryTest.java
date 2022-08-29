package com.codesoom.assignment.domain;

import com.codesoom.assignment.Fixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {
    private static final User USER = User.builder()
            .email(Fixture.EMAIL)
            .password(Fixture.PASSWORD)
            .name(Fixture.USER_NAME)
            .build();

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmailTest() {
        userRepository.save(USER);

        assertTrue(userRepository.existsByEmail(Fixture.EMAIL));
    }
}
