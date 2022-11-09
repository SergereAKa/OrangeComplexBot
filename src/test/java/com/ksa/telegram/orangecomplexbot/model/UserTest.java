package com.ksa.telegram.orangecomplexbot.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTest {
    private static final long CORRECT_USER_ID = 1L;
    private static final long WRONG_USER_ID = 2L;

    private static final String CORRECT_ROLE = "ADMIN";
    private static final String WRONG_ROLE = RandomStringUtils.randomAlphabetic(10);


    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void init(){
        User user = new User();
        user.setId(CORRECT_USER_ID);
        user.setRolies(CORRECT_ROLE);
        userRepository.save(user);

        user = new User();
        user.setId(WRONG_USER_ID);
        user.setRolies(WRONG_ROLE);


    }



    @Test
    void shouldReturnCorrectRole() {
        User user = userRepository.findById(CORRECT_USER_ID).orElse(null);
        assertEquals(CORRECT_ROLE, user.getRolies());
    }
    @Test
    void shouldReturnNotCorrectRole() {
        User user = userRepository.findById(CORRECT_USER_ID).orElse(null);
        assertNotEquals(WRONG_ROLE, user.getRolies());
    }

    @Test
    void shouldCorrectCheckRole(){
        User user =  userRepository.findById(CORRECT_USER_ID).orElse(null);
        assertTrue(user.checkRole(CORRECT_ROLE));
    }
    @Test
    void shouldNotCorrectCheckRole(){
        User user =  userRepository.findById(CORRECT_USER_ID).orElse(null);
        assertFalse(user.checkRole(WRONG_ROLE));
    }

}