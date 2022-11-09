package com.ksa.telegram.orangecomplexbot.service;

import com.ksa.telegram.orangecomplexbot.component.BotMessenger;
import com.ksa.telegram.orangecomplexbot.component.StartBotMessenger;
import com.ksa.telegram.orangecomplexbot.config.BotConfig;
import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import com.ksa.telegram.orangecomplexbot.model.UserRepositoryTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class TelegramBotTest {
    private final String TOKEN = RandomStringUtils.randomAlphabetic(20);
    private final long  OWNERID = System.currentTimeMillis();
    private final String USERNAME = RandomStringUtils.randomAlphabetic(10);

    private TelegramBot bot;
    @Autowired
    private UserRepository userRepository;
    //@Mock
    private  BotConfig config;

    private Update update;
    private Message message;
    private Chat chat;
    private BotMessenger messenger;

    @BeforeEach
    void init(){
        config = Mockito.mock(BotConfig.class);
        when(config.getBotToken()).thenReturn(TOKEN);
        when(config.getBotUserName()).thenReturn(USERNAME);
        bot = new TelegramBot(config, userRepository, null);

        update = Mockito.mock(Update.class);
        chat= Mockito.mock(Chat.class);
        message = Mockito.mock(Message.class);
        when(chat.getFirstName()).thenReturn(UserRepositoryTest.FIRST_NAME);
        when(chat.getLastName()).thenReturn(UserRepositoryTest.LAST_NAME);
        when(chat.getUserName()).thenReturn(UserRepositoryTest.USER_NAME);
        when(message.getChat()).thenReturn(chat);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(System.currentTimeMillis());
        when(update.getMessage()).thenReturn(message);
        messenger = new StartBotMessenger(userRepository);


    }

    @Test
    void shoulReturnUserName() {
        assertEquals(USERNAME, bot.getBotUsername());
    }

    @Test
    void shouleReturnToken(){
        assertEquals(TOKEN, bot.getBotToken());
    }

    @Test
    void shouldSaveChatUser(){

        //System.out.printf("update=%s, message=%s, chatId=%s", update, update.getMessage(), update.getMessage().getChatId());

        bot.onUpdateReceived(update);

        Optional userOptinal = userRepository.findById(update.getMessage().getChatId());

        assertFalse(userOptinal.isEmpty());
        User user = (User)userOptinal.orElse(null);

        assertEquals(UserRepositoryTest.FIRST_NAME, user.getFirstName());
        assertEquals(UserRepositoryTest.LAST_NAME, user.getLastName());
        assertEquals(UserRepositoryTest.USER_NAME, user.getUserName());



    }
}