package com.ksa.telegram.orangecomplexbot.service;

import com.ksa.telegram.orangecomplexbot.component.BotMessenger;
import com.ksa.telegram.orangecomplexbot.component.DictionaryBotMessenger;
import com.ksa.telegram.orangecomplexbot.component.StartBotMessenger;
import com.ksa.telegram.orangecomplexbot.component.TextBotMessenger;
import com.ksa.telegram.orangecomplexbot.config.BotConfig;
import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config ;
    private final List<BotMessenger> messengers = new ArrayList<>();
    public TelegramBot(BotConfig config, UserRepository userRepository, DictionaryRepository dictionaryRepository){
        this.config = config;

        messengers.add(new StartBotMessenger(userRepository));
        messengers.add(new DictionaryBotMessenger(userRepository, dictionaryRepository));
        messengers.add(new TextBotMessenger(userRepository, dictionaryRepository));


    }



    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    /************************************************************
     *
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
//        BotMessenger messenger =  new StartBotMessenger();
//        messenger.setUpdate(update);
//        if(messenger.isAllow()){
//            sendMessage(messenger.execute());
//            return;
//        }

        log.info("chatId:{}, userName:{}, message:{}",update.getMessage().getChatId(), update.getMessage().getChat().getUserName(), update.getMessage().getText());

        messengers.forEach(m->{
            m.setUpdate(update);
            if(m.isAllow()){
                sendMessage(m.execute());
                m.saveUser();
                if(m.isExclusive()){
                    return;
                }
            }
        });



    }

    @Override
    public String getBotUsername() {
        return config.getBotUserName();
    }


    public void sendMessage(SendMessage message){
        try{
            execute(message);
        } catch (TelegramApiException e){
            log.error("Error send message:" + e.getMessage());
        }
    }

}
