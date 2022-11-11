package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.Dictionary;
import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class TextBotMessengerTest {

    private static final String TEXT_MESSAGE    = "Подскажите телефон Территории";
    private static final String DICT_CODE       = "ТЕЛЕФОН_ТЕРРИТОРИИ";
    private static final String DICT_MATCH      = "(?=^.*телефон)(?=^.*территор)";
    private static final String DICT_TEXT       = "телефоны: 123456789, 87946513";
    private static final long   CHAT_ID         = 132456;

    private static final String USER_NAME       = RandomStringUtils.randomAlphabetic(20);
    private static final String LAST_NAME       = RandomStringUtils.randomAlphabetic(20);
    private static final String FIRST_NAME      = RandomStringUtils.randomAlphabetic(20);

    private Message message;
    private Update update;
    private Chat chat;

    private TextBotMessenger messenger;

    @Autowired
    DictionaryRepository dictionaryRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init(){
        chat    =   Mockito.mock(Chat.class);
        message =   Mockito.mock(Message.class);
        update  =   Mockito.mock(Update.class);

        when(chat.getId()).thenReturn(CHAT_ID);
        when(chat.getLastName()).thenReturn(LAST_NAME);
        when(chat.getFirstName()).thenReturn(FIRST_NAME);
        when(chat.getUserName()).thenReturn(USER_NAME);

        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getText()).thenReturn(TEXT_MESSAGE);
        when(message.getChat()).thenReturn(chat);

        when(update.getMessage()).thenReturn(message);

        User user   =   new User();
        user.setId(CHAT_ID);
        user.setUserName(USER_NAME);
        user.setLastName(LAST_NAME);
        user.setFirstName(FIRST_NAME);

        userRepository.save(user);

        Dictionary dictionary = new Dictionary();
        dictionary.setText(DICT_TEXT);
        dictionary.setCode(DICT_CODE);
        dictionary.setMatcher(DICT_MATCH);

        dictionaryRepository.save(dictionary);


        messenger = new TextBotMessenger(userRepository, dictionaryRepository);
        messenger.setUpdate(update);

    }


    @Test
    void isAllowShouldTrue() {
        assertTrue(messenger.isAllow());
    }

    @Test
    void isExclusive() {
        assertFalse(messenger.isExclusive());
    }

    @Test
    void execute() {
//        SendMessage sendMessage = messenger.execute();
//        assertEquals(TEXT_MESSAGE, sendMessage.getText());
    }

}