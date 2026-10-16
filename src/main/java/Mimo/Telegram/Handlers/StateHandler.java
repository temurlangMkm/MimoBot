package Mimo.Telegram.Handlers;

import Mimo.Telegram.Entity.MessageToSend;
import Mimo.Telegram.Service.ChatService;
import Mimo.Telegram.Service.OldUserService;
import Mimo.Telegram.Service.UserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Component;

@Component
public class StateHandler {

    private final OldUserService oldUserService;

    private final UserService userService;
    private final ChatService chatService;

    public StateHandler(OldUserService oldUserService) {
        this.oldUserService = oldUserService;
    }

    public MessageToSend handle(String message, Long  id){

        MessageToSend messageToSend =  switch (state) {
            case CHOICE_GROUP -> userService.saveGroup(id, message);
            case UNREGISTERED -> userService.registration(id);
            case SEND_SCHEDULE -> userService.setSchedule(message, id);
            default -> "Error. Your message could not be processed at this moment.";
        };

    }

    public MessageToSend welcome(Long chatId) {
        return chatId > 0 ? userService.newTgUser(chatId) : chatService.newTgChat(chatId);
    }
}
