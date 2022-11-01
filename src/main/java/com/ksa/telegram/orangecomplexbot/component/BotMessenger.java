package com.ksa.telegram.orangecomplexbot.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@Getter
@Setter
public abstract class BotMessenger {
    protected   Update update;
    protected   Message message;

    public abstract boolean isAllow();
    public abstract boolean isExclusive();
    public abstract SendMessage execute();

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


}
