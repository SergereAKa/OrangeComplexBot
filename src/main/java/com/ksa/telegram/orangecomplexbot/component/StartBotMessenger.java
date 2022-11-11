package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

@Component
@Slf4j
public class StartBotMessenger extends  BotMessenger implements IBotMessenger{
//    public StartBotMessenger(Update update) {
//        super(update);
//    }


    public StartBotMessenger(UserRepository userRepository){
        super(userRepository);
    }

    @Override
    public boolean isAllow() {
//        return update.getMessage().getText().equals("/start");
        return this.getUpdate().getMessage().getText().equals("/start");

    }

    @Override
    public boolean isExclusive() {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public SendMessage execute() {

        final long chatId =  this.getUpdate().getMessage().getChatId();

//        if(userRepository.findById(chatId).isEmpty()){
//            User user = new User();
//            user.setUserName(update.getMessage().getChat().getUserName());
//            user.setFirstName(update.getMessage().getChat().getFirstName());
//            user.setLastName(update.getMessage().getChat().getLastName());
//            user.setDateReg(new Timestamp(System.currentTimeMillis()));
//            userRepository.save(user);
//            log.info("Add user with id={}, firstName={}, lastName={}, userName={}", user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
//        }


        final String text =  String.format("Hi, %s! You are registered!", this.getUpdate().getMessage().getChat().getFirstName());



        return this.prepareSendMessage(chatId, text);
    }
}
