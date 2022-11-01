package com.ksa.telegram.orangecomplexbot.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StartBotMessengerTest {
    private final String FIRSTNAME = "Иван Бровкин";
    private final String MSGFIRSTNAME = String.format("Hi, %s! You are registered!", FIRSTNAME);
    private final long CHATID = 123456;


    private StartBotMessenger messenger;
    private Update update;
    private Message  message;
    private Chat chat;
    @BeforeEach
    void init(){


        chat= Mockito.mock(Chat.class);
        message = Mockito.mock(Message.class);
        update = Mockito.mock(Update.class);
        when(chat.getFirstName()).thenReturn(FIRSTNAME);
        when(message.getChat()).thenReturn(chat);
        when(message.getText()).thenReturn("/start");
        when(update.getMessage()).thenReturn(message);
        messenger = new StartBotMessenger();
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

}