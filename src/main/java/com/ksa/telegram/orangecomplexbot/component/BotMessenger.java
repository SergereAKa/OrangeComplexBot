package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public  class BotMessenger implements IBotMessenger{

    private   Update update;
    private   final UserRepository userRepository;

    public SendMessage prepareSendMessage(long chatId, String messageText){
        return prepareSendMessage(chatId, messageText, null);
    }

    public SendMessage prepareSendMessage(long chatId, String messageText, ReplyKeyboardMarkup keyboardMarkup){
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
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

    @Override
    public boolean isAllow() {
        return false;
    }

    @Override
    public boolean isExclusive() {
        return false;
    }

    @Override
    public SendMessage execute() {
        return null;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
