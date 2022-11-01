package com.ksa.telegram.orangecomplexbot.service;

import com.ksa.telegram.orangecomplexbot.config.BotConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TelegramBotTest {
    private final String TOKEN = "13246546874687dfsfs";
    private final long  OWNERID = 15465498;
    private final String USERNAME = "sdfsdfsdf";

    private TelegramBot bot;

    //@Mock
    private  BotConfig config;

    @BeforeEach
    void init(){
        config = Mockito.mock(BotConfig.class);
        when(config.getBotToken()).thenReturn(TOKEN);
        when(config.getBotUserName()).thenReturn(USERNAME);
        bot = new TelegramBot(config);
    }

    @Test
    void shoulReturnUserName() {
        assertEquals(USERNAME, bot.getBotUsername());
    }

    @Test
    void shouleReturnToken(){
        assertEquals(TOKEN, bot.getBotToken());
    }
}