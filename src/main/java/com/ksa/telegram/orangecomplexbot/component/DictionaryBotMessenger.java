package com.ksa.telegram.orangecomplexbot.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksa.telegram.orangecomplexbot.exception.OrangeComplexException;
import com.ksa.telegram.orangecomplexbot.model.Dictionary;
import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;

public class DictionaryBotMessenger extends BotMessenger{
    private static final List<String> COMMANDS = Arrays.asList("/upddict", "/deldict");
    private static final String DELIM = "&";
    private static final String ROLE = "ADMIN_DICT";
    private final DictionaryRepository dictionaryRepository;

    /********************************************************
     *
     * @param userRepository
     * @param dictionaryRepository
     */
    public DictionaryBotMessenger(UserRepository userRepository, DictionaryRepository dictionaryRepository){
        super(userRepository);
        this.dictionaryRepository = dictionaryRepository;
    }

    /************************************************
     *
     * @return
     */
    public String getCommand(){
        String command = update.getMessage().getText().split(DELIM)[0];



        if(command.isEmpty()){
            throw new OrangeComplexException("The command was not found in the message(%s)", update.getMessage().getText());
        }

//        if(!command.equals(COMMAND)) {
//            throw new OrangeComplexException("The command in the message was incorrect(%s)", update.getMessage().getText());
//
//        }
        return command;
    }

    /***********************************************************
     *
     * @return
     */
    public String getPrametersMessage() {
        try{
            final String message = update.getMessage().getText();
            return  message.split(DELIM)[1];
        } catch(ArrayIndexOutOfBoundsException e){
            throw new OrangeComplexException("The message for the parameters was not found in the message \"%s\"", update.getMessage().getText());
        }
    }


    public Dictionary createParametersObject()   {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(getPrametersMessage(), Dictionary.class);
        } catch(JsonProcessingException jpe){
            throw new OrangeComplexException("Serialization error message \"%s\" for parameters object", getPrametersMessage());
        }
    }


    /*************************************************
     *
     * @return
     */
    @Override
    public boolean isAllow() {
        return COMMANDS.contains(getCommand());
    }

    /*************************************************
     *
     * @return
     */
    @Override
    public boolean isExclusive() {
        return true;
    }


    /*************************************************
     *
     * @return
     */
    @Override
    public boolean isAdmin() {
        final long chatId = update.getMessage().getChatId();
        final User user =  userRepository.findById(chatId).orElseThrow(()-> new RuntimeException("User with id="+chatId + " not found."));
        final String rolies = "," + user.getRolies() + ",";
        return rolies.contains(","+ROLE + ",");
    }


    public String getRole(){
        return ROLE;
    }



//    public Dictionary createParametersObject() {
//
//    }

    /*************************************************
     *
     * @return
     */
    @Override
    public SendMessage execute() {

        final long chatId =  update.getMessage().getChatId();

        if(!isAdmin()){
            return this.prepareSendMessage(chatId, "This operation is not supported");
        }

        Dictionary dictionary = createParametersObject();
        dictionary = dictionaryRepository.findByCode(dictionary.getCode()).orElse(dictionary);


        String text =  "Congratulations! The message with the code \"%s\" has been %s the dictionary";

        if(getCommand().equals("/deldict") && (dictionary.getId() == null ||dictionary.getId() == 0 )){
            text = String.format("Message with code \"%s\" not found in the dictionary", dictionary.getCode());
        } else if (getCommand().equals("/deldict")){
            text = String.format(text, dictionary.getCode(), "deleted from");
//            dictionaryRepository.deleteByCode(dictionary.getCode());
            dictionaryRepository.delete(dictionary);

        } else {
            text = String.format(text, dictionary.getCode(), "saved to");
            dictionaryRepository.save(dictionary);
        }



        return this.prepareSendMessage(chatId, text);
    }



}
