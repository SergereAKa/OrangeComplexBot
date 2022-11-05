package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DictionaryBotMessenger extends BotMessenger{

    private final DictionaryRepository dictionaryRepository;

    public DictionaryBotMessenger(UserRepository userRepository, DictionaryRepository dictionaryRepository){
        super(userRepository);
        this.dictionaryRepository = dictionaryRepository;
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
