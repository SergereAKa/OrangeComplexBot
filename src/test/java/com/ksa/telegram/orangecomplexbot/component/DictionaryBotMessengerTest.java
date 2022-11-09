package com.ksa.telegram.orangecomplexbot.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksa.telegram.orangecomplexbot.exception.OrangeComplexException;
import com.ksa.telegram.orangecomplexbot.model.Dictionary;
import com.ksa.telegram.orangecomplexbot.model.DictionaryRepository;
import com.ksa.telegram.orangecomplexbot.model.User;
import com.ksa.telegram.orangecomplexbot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**************************************
 *Компонента редактировать словаря
 * Операции проверки
 * 1. Проверка разрешения работы с компоенентой   isAllow           shouldIsAllowEqualTrue
 *                                                                  shouldIsAllowEqualFalse
 * 2. Разрешение работать только с этой компонентой isExclusive     shouldIsExclusiveEqualTrue
 * 3. Доступ к работе только с этой компонентой     isAdmin         shouldIsAdminEqualTrue
 *                                                                  shouldIsAdminEqualFalse
 * 4. Получить сообщение для редактировании                         shouldMessageIsNotEmpty
 *                                                                  shouldThrowEmptyMessage
 * 5. Получить объект с сообщением                                  shouldCreatedDictionaryFromMessage
 *                                                                  shouldThrowCreatedDictionaryFromMessage
 * 6. Проверить наличие все полей в сообщении                       shouldCheckedDictianaryAllFieldInObjectMessage
 * 7. Проверить поиск по коду словаря                               shouldFounddDictByCode
 *                                                                  shouldNotFounddDictByCode
 */

@DataJpaTest
@Slf4j
class DictionaryBotMessengerTest {

    private static final long USER_ID_ADMIN =  1;
    private static final long USER_ID_NOT_ADMIN =  2;
    private static final String ROLE_CODE_CORRECT = "ADMIN_DICT";
    private static final String ROLE_CODE_WRONG = RandomStringUtils.randomAlphabetic(10);

    private static final String DICT_CODE = "ТЕЛЕФОНЫ";
    private static final String DICT_TEXT = "Текст для словаря";
    private static final String DICT_MATCHER = "телефон";

    private static final String SEND_MESSAGE_UPDATE = String.format("Congratulations! The message with the code \"%s\" has been saved to the dictionary", DICT_CODE);
    private static final String SEND_MESSAGE_DELETE =  String.format("Congratulations! The message with the code \"%s\" has been deleted from the dictionary", DICT_CODE);
    private static final String SEND_MESSAGE_WRONG   = "This operation is not supported";

    private static String COMMAND_UPDATE = "/upddict";
    private static String COMMAND_DELETE = "/deldict";

    private String dictionaryMessage ;

    private DictionaryBotMessenger messenger;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    private Update update = mock(Update.class);
    private Message message = mock(Message.class);
    private Chat chat = mock(Chat.class);

    private String commandUpdate = COMMAND_UPDATE + "&" + dictionaryMessage;
    private String commandDelete = COMMAND_DELETE + "&" + dictionaryMessage;

    @BeforeEach
    void init() throws Exception{

        User user = new User();
        user.setId(USER_ID_ADMIN);
        user.setRolies(ROLE_CODE_CORRECT);
        userRepository.save(user);

        user =  new User();
        user.setId(USER_ID_NOT_ADMIN);
        user.setRolies(ROLE_CODE_WRONG);
        userRepository.save(user);

        final Dictionary dictionary = new Dictionary();
        dictionary.setCode(DICT_CODE);
        dictionary.setText(DICT_TEXT);
        dictionary.setMatcher(DICT_MATCHER);

        final ObjectMapper objectMapper = new ObjectMapper();
        dictionaryMessage = objectMapper.writeValueAsString(dictionary);

        commandUpdate = COMMAND_UPDATE + "&" + dictionaryMessage;
        commandDelete = COMMAND_DELETE + "&" + dictionaryMessage;
//log.info(commandUpdate);


        //Chat chat = mock(Chat.class);
        //Message message = mock(Message.class);
        //Update update = mock(Update.class);
        when(message.getChatId()).thenReturn(USER_ID_ADMIN);
        when(message.getText()).thenReturn(commandUpdate);
        when(update.getMessage()).thenReturn(message);


        messenger = new DictionaryBotMessenger(userRepository,dictionaryRepository);
        messenger.setUpdate(update);

//        dictionaryRepository.save(dictionary);

    }


    @Test
    void shouldReturnCorrectCommand(){
        assertEquals(COMMAND_UPDATE, messenger.getCommand());
    }

    @Test
    void shouldReturnNotCorrectCommand(){
        when(messenger.getUpdate().getMessage().getText()).thenReturn("test");
        assertNotEquals(COMMAND_UPDATE, messenger.getCommand());
    }


    @Test
    void shouldIsAllowEqualTrueForUpdate(){
        //String command = messenger.update.getMessage().getText().split(" ")[0];
        assertTrue(messenger.isAllow());
    }

    @Test
    void shouldAllowEqualTrueForDelete(){
        when(update.getMessage().getText()).thenReturn(commandDelete);
        assertTrue(messenger.isAllow());
    }

    @Test
    void shouldIsAllowEqualFalse(){
        when(message.getText()).thenReturn("/test");
        when(update.getMessage()).thenReturn(message);
        messenger.setUpdate(update);

        assertFalse(messenger.isAllow());
    }


    @Test
    void shouldIsExclusiveEqualTrue(){
        assertTrue(messenger.isExclusive());
    }
    @Test
    void shouldIsAdminEqualTrue(){
        assertTrue(messenger.isAdmin());
    }

    @Test
    void shouldIsAdminEqualFalse(){
        when(messenger.getUpdate().getMessage().getChatId()).thenReturn(USER_ID_NOT_ADMIN);
        assertFalse(messenger.isAdmin());
    }

    @Test
    void shouldReturnCorrectRole(){
        assertEquals(ROLE_CODE_CORRECT, messenger.getRole());
    }

    @Test
    void shouldReturnNotCorrectRole(){
        //when(messenger.getUpdate().getMessage().getChatId()).thenReturn(US)
        assertNotEquals(ROLE_CODE_WRONG, messenger.getRole());
    }

    @Test
    void shouldTextOfUpdateDictMessageIsNotEmpty(){
        assertFalse(messenger.getCommand().isEmpty());
    }

//    @Test
//    void shouldThrowEmptyCommand(){
//        when(message.getText()).thenReturn("/command");
//        when(update.getMessage()).thenReturn(message);
//        messenger.setUpdate(update);
//        assertThrows(OrangeComplexException.class, ()->messenger.getCommand());
//    }

    @Test
    void shouldReciveDictionaryMessage(){
        assertEquals(dictionaryMessage, messenger.getPrametersMessage());

    }

    @Test
    void shouldThrowReceiveParametersMessage(){
        when(message.getText()).thenReturn("/command");
        when(update.getMessage()).thenReturn(message);
        messenger.setUpdate(update);

        assertThrows(OrangeComplexException.class, ()-> messenger.getPrametersMessage());

    }

    @Test
    void shouldCreatedParametersObjectFromMessage(){
        Dictionary dictionary = messenger.createParametersObject();
        assertEquals(DICT_CODE, dictionary.getCode());
        assertEquals(DICT_MATCHER, dictionary.getMatcher());
        assertEquals(DICT_TEXT, dictionary.getText());
    }

    @Test
    void shouldThrowCreatedParametersFromMessage(){
        when(message.getText()).thenReturn("/command&{fdfdfd, fdfd,fdfd}");
        when(update.getMessage()).thenReturn(message);
        messenger.setUpdate(update);
        assertThrows(OrangeComplexException.class, ()->messenger.createParametersObject());
    }



    /***********************************************************
     * checking work with administrator rights
     */
    @Test
    void shouldExecutedWithAdminRightsAndAddMessageSettingToDictionary(){
        SendMessage sendMessage = messenger.execute();
        Dictionary dictionary =  dictionaryRepository.findByCode(DICT_CODE).orElse(null);
        assertEquals(DICT_CODE, dictionary.getCode());
        assertEquals(SEND_MESSAGE_UPDATE, sendMessage.getText());
    }

    /***********************************************************
     * checking work with user rights
     */
    @Test
    void shouldExecutedWithUserRightsAndNoAddMessageSettingToDictionary(){
        when(messenger.getUpdate().getMessage().getChatId()).thenReturn(USER_ID_NOT_ADMIN);
        SendMessage sendMessage = messenger.execute();
        assertEquals(SEND_MESSAGE_WRONG, sendMessage.getText());
        Dictionary dictionary = dictionaryRepository.findByCode(DICT_CODE).orElse(null);
        assertNull(dictionary);
    }

    @Test
    void shoulDeleteRowByCodeItDictionary(){
        when(message.getText()).thenReturn(commandDelete);
        when(update.getMessage()).thenReturn(message);
        messenger.setUpdate(update);
        Dictionary dictionary = messenger.createParametersObject();
        dictionaryRepository.save(dictionary);
        //when(messenger.getCommand()).thenReturn(COMMAND_DELETE);
        SendMessage sendMessage = messenger.execute();
        assertEquals(SEND_MESSAGE_DELETE, sendMessage.getText());
        dictionary = dictionaryRepository.findByCode(DICT_CODE).orElse(null);
        assertNull(dictionary);

    }

/*



    @Test
    void shouldCheckedDictianaryAllFieldInObjectMessage(){
        fail();
    }
    @Test
    void shouldFounddDictByCode(){
        fail();
    }
    @Test
    void shouldNotFounddDictByCode(){
        fail();
    }
*/











}