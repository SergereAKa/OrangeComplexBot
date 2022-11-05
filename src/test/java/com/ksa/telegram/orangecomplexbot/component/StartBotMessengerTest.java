package com.ksa.telegram.orangecomplexbot.component;

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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
class StartBotMessengerTest {
    //private final String FIRST_NAME = "Иван Бровкин";
    //private final long CHATID = 123456;

    public static final String USER_NAME = RandomStringUtils.randomAlphabetic(10); // anyString();   //(String.class); //RandomStringUtils.randomAlphabetic(10);
    public static final String LAST_NAME = RandomStringUtils.randomAlphabetic(10); //anyString(); //any(String.class); //RandomStringUtils.randomAlphabetic(10);
    public static final String FIRST_NAME = RandomStringUtils.randomAlphabetic(10); //anyString(); //any(String.class); // RandomStringUtils.randomAlphabetic(10);
    public static final Timestamp DATE_REG = new Timestamp(System.currentTimeMillis()); //anyTimestamp();//any(Timestamp.class); // new Timestamp(System.currentTimeMillis());
    public static final Long CHATID = System.currentTimeMillis();

    private final String MSGFIRSTNAME = String.format("Hi, %s! You are registered!", FIRST_NAME);


    @Autowired
    private UserRepository userRepository;

    private StartBotMessenger messenger;
    private Update update;
    private Message  message;
    private Chat chat;
    @BeforeEach
    void init(){


        chat= Mockito.mock(Chat.class);
        message = Mockito.mock(Message.class);
        update = Mockito.mock(Update.class);

//        when(chat.getFirstName()).thenReturn(FIRSTNAME);
        when(chat.getUserName()).thenReturn(USER_NAME);
        when(chat.getFirstName()).thenReturn(FIRST_NAME);
        when(chat.getLastName()).thenReturn(LAST_NAME);
        when(chat.getId()).thenReturn(CHATID);

        when(message.getChat()).thenReturn(chat);
        when(message.getChatId()).thenReturn(CHATID);
        when(message.getText()).thenReturn("/start");

        when(update.getMessage()).thenReturn(message);

        messenger = new StartBotMessenger(userRepository);
        messenger.setUpdate(update);

    }

    @Test
    void shouldAllow() {
        assertTrue(messenger.isAllow());
    }

    @Test
    void shouldNotAllow() {

        when(message.getText()).thenReturn("/empty");
        when(update.getMessage()).thenReturn(message);
        assertTrue(!messenger.isAllow());
    }


    @Test
    void shouldExecuteReturnCorrectMessage() {
        SendMessage message = messenger.execute();
        assertEquals(MSGFIRSTNAME, message.getText());
    }

    @Test
    void shouldExclusiveTrue(){
        assertTrue(messenger.isExclusive());
    }

    @Test
    void shouldReturnSendMessageWithoutKeyBoard(){
        SendMessage sendMessage = messenger.prepareSendMessage(CHATID, MSGFIRSTNAME);

        assertEquals(CHATID, Long.parseLong(sendMessage.getChatId()));
        assertEquals(MSGFIRSTNAME, sendMessage.getText());

    }

    @Test
    void shouldReturnSendMessageWithKeyBoard(){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        final List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("key1");
        row1.add("key2");

        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2 = new KeyboardRow();
        row2.add("key3");
        row2.add("key4");

        keyboardRows.add(row2);

        keyboardMarkup.setKeyboard(keyboardRows);

        SendMessage sendMessage = messenger.prepareSendMessage(CHATID, MSGFIRSTNAME, keyboardMarkup);
        keyboardMarkup = (ReplyKeyboardMarkup)sendMessage.getReplyMarkup();

        assertEquals(CHATID, Long.parseLong(sendMessage.getChatId()));
        assertEquals(MSGFIRSTNAME, sendMessage.getText());

        assertEquals(2, keyboardMarkup.getKeyboard().size());
        assertArrayEquals(row1.toArray(), keyboardMarkup.getKeyboard().get(0).toArray());
        assertArrayEquals(row2.toArray(), keyboardMarkup.getKeyboard().get(1).toArray());

    }

    @Test
    void shouldSavedUser(){
        Message message = messenger.getMessage();

        Long chatId = messenger.getMessage().getChatId();
        messenger.saveUser();
        User user = userRepository.findById(chatId).get();

        assertEquals(USER_NAME, user.getUserName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(FIRST_NAME,user.getFirstName());
        assertEquals(CHATID, user.getId());

    }

    @Test
    void shouldMessageEqual(){
        assertEquals(CHATID, messenger.getMessage().getChatId());
    }

    @Test
    void shoulChatEqual(){
        assertEquals(CHATID, messenger.getMessage().getChat().getId());
    }

    @Test
    void shouldIsAdminFalse(){
        assertFalse(messenger.isAdmin());
    }
}