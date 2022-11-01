package com.ksa.telegram.orangecomplexbot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartBotMessenger extends  BotMessenger{

//    public StartBotMessenger(Update update) {
//        super(update);
//    }


    @Override
    public boolean isAllow() {

        return update.getMessage().getText().equals("/start");
    }

    @Override
    public boolean isExclusive() {
        return true;
    }

    @Override
    public SendMessage execute() {

        final long chatId =  update.getMessage().getChatId();
        final String text =  String.format("Hi, %s! You are registered!", update.getMessage().getChat().getFirstName());

        return this.prepareSendMessage(chatId, text);
    }
}
