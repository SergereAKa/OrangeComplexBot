package com.ksa.telegram.orangecomplexbot.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotConfigTest {
    private final String TOKEN = "13246546874687dfsfs";
    private final long  OWNERID = 15465498;
    private final String USERNAME = "sdfsdfsdf";
    private  BotConfig config;
    @BeforeEach
    void setUp() {
        config = new BotConfig();
        config.setBotToken(TOKEN);
        config.setBotUserName(USERNAME);
        config.setOwnerId(OWNERID);;

    }

    @Test
    void checkGetMethods() {
        assertEquals(USERNAME, config.getBotUserName());
        assertEquals(TOKEN, config.getBotToken());
        assertEquals(OWNERID, config.getOwnerId());
    }


    @Test
    void checkSetMethods() {
        config.setBotUserName(USERNAME);
        config.setBotToken(TOKEN);
        config.setOwnerId(OWNERID);

        assertEquals(USERNAME, config.getBotUserName());
        assertEquals(TOKEN, config.getBotToken());
        assertEquals(OWNERID, config.getOwnerId());

    }

}