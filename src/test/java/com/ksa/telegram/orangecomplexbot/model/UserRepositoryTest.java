package com.ksa.telegram.orangecomplexbot.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
public class UserRepositoryTest {
    public static final String USER_NAME = RandomStringUtils.randomAlphabetic(10);
    public static final String LAST_NAME = RandomStringUtils.randomAlphabetic(10);
    public static final String FIRST_NAME =  RandomStringUtils.randomAlphabetic(10);
    public static final Timestamp DATE_REG = new Timestamp(System.currentTimeMillis());
    public static final Long ID = System.currentTimeMillis();
    @Autowired
    private UserRepository userRepository;

    User savedUser ;

    @BeforeEach
    void init(){
        savedUser = new User();
        savedUser.setId(ID);
        savedUser.setUserName(USER_NAME);
        savedUser.setFirstName(FIRST_NAME);
        savedUser.setDateReg(DATE_REG);
        savedUser.setLastName(LAST_NAME);

        savedUser = userRepository.save(savedUser);
    }

    @Test
    void testCreatedUser(){

        assertThat(savedUser.getId()).isGreaterThan(0);
        assertEquals(USER_NAME, savedUser.getUserName());
        assertEquals(LAST_NAME, savedUser.getLastName());
        assertEquals(FIRST_NAME, savedUser.getFirstName());
        assertEquals(DATE_REG, savedUser.getDateReg());

    }

    @Test
    void testFindUser(){
        long id = savedUser.getId();

        User user = userRepository.findById(id).get();
        assertEquals(id, user.getId());
        assertEquals(USER_NAME, user.getUserName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(DATE_REG, user.getDateReg());



    }

    @Test
    void testNoFindUser(){
        assertTrue(userRepository.findById(0L).isEmpty());
    }

    @Test
    void testDeleteUserByUser(){
        userRepository.delete(savedUser);
        assertTrue(userRepository.findById(savedUser.getId()).isEmpty());

    }

    @Test
    void testDeleteUserByUserId(){
        userRepository.deleteById(savedUser.getId());
        assertTrue(userRepository.findById(savedUser.getId()).isEmpty());

    }

}