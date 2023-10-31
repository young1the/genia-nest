package com.chunjae.nest.domain.user.repository;

import com.chunjae.nest.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void test() {
        String userId = "";
        User testUser = userRepository.findByUserId(userId);
        System.out.println(testUser);

    }

}