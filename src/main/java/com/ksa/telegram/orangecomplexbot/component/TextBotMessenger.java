package com.ksa.telegram.orangecomplexbot.component;

import com.ksa.telegram.orangecomplexbot.model.Dictionary;
import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TextBotMessenger extends BotMessenger implements IBotMessenger{
    private final DictionaryRepository dictionaryRepository;

    public TextBotMessenger(UserRepository userRepository,  DictionaryRepository dictionaryRepository) {
        super(userRepository);
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public boolean isAllow() {
        return true;
    }

    @Override
    public boolean isExclusive() {

        //final Pattern pattern = Pattern.compile("");
        //pattern.matcher()
        return false;
    }

    @Override
    public SendMessage execute() {

        String message =  this.getUpdate().getMessage().getText();
        String result = null;

        for(Dictionary dictionary:dictionaryRepository.findAll()){
            Pattern pattern  = Pattern.compile(dictionary.getMatcher());
            Matcher matcher = pattern.matcher(message);
            if(matcher.find()){
                result = dictionary.getText();
                break;
            }
        }

        if (result == null){
            return null;
        }

        return prepareSendMessage(this.getUpdate().getMessage().getChatId(), result);

//        result = dictionaryRepository.findAll()
//                            .stream()
//                            .filter(dict->{
//                                Pattern pattern =  Pattern.compile(dict.getMatcher());
//                                Matcher matcher =  pattern.matcher(message);
//                                return matcher.find();
//                            })
//                            .distinct()
//                            .limit(1)
//                .map(dict->dict.getText());



    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
