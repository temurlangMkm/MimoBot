package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.ChatService;
import Mimo.Telegram.Service.UserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Component;

@Component
public class StateHandler {

    private final UserService userService;
    private final ChatService chatService;

    public StateHandler(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    public String handle(String message, Long chatId, State state){

        return switch (state) {
            case CHOICE_GROUP -> chatId>0 ? userService.saveGroup(chatId, message) : chatService.saveGroup(chatId, message);
            case UNREGISTERED -> chatId>0 ? userService.registration(chatId) : chatService.registration(chatId);
            case SEND_SCHEDULE -> chatId>0 ? userService.setSchedule(message, chatId) : "";
            default -> "Error. Your message could not be processed at this moment.";
        };

    }
}
