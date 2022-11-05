package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.sql.Timestamp;

@Component
@Getter
@Setter
@Slf4j
public abstract class BotMessenger {

    protected   Update update;
    protected   Message message;
    protected   UserRepository userRepository;

    public BotMessenger(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public abstract boolean isAllow();
    public abstract boolean isExclusive();
    public abstract SendMessage execute();
    public abstract boolean isAdmin();

    public SendMessage prepareSendMessage(long chatId, String messageText){
        return prepareSendMessage(chatId, messageText, null);
    }

    public SendMessage prepareSendMessage(long chatId, String messageText, ReplyKeyboardMarkup keyboardMarkup){
        final SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    public Message getMessage(){
        return update.getMessage();
    }

    public void saveUser(){
        if(userRepository.findById(update.getMessage().getChatId()).isEmpty()){
            User user = new User();
            user.setId(update.getMessage().getChatId());
            user.setUserName(update.getMessage().getChat().getUserName());
            user.setFirstName(update.getMessage().getChat().getFirstName());
            user.setLastName(update.getMessage().getChat().getLastName());
            user.setDateReg(new Timestamp(System.currentTimeMillis()));
            user = userRepository.save(user);
            log.info("Add user with id={}, firstName={}, lastName={}, userName={}", user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
        }

    }

}
