package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class DictionaryBotMessengerTest {

    private DictionaryBotMessenger messenger;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DictionaryRepository dictionaryRepository;


    @BeforeEach
    void init(){

        Chat chat = Mockito.mock(Chat.class);
        Message message = Mockito.mock(Message.class);
        Update update = Mockito.mock(Update.class);

        when(message.getText()).thenReturn("/editdict");



        messenger = new DictionaryBotMessenger(userRepository,dictionaryRepository);
    }

    @Test
    void isAllow() {
        fail();
    }

    @Test
    void isExclusive() {
        fail();
    }

    @Test
    void isAdmin() {
        fail();
    }
}