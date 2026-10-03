package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.UserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Service;

@Service
public class ChatHandler {

    private final CommandHandler commandHandler;
    private final StateHandler stateHandler;
    private final UserService userService;

    public ChatHandler(CommandHandler commandHandler, StateHandler stateHandler, UserService userService){
        this.commandHandler = commandHandler;
        this.stateHandler = stateHandler;
        this.userService = userService;
    }

    public String handle (String message, Long chatId){

        if (message.startsWith("/")) {
            return commandHandler.handle(message, chatId);
        }

        State userState = userService.getState(chatId);

        if(userState != State.NONE) {
            return stateHandler.handle(message, chatId, userState);
        }

        return "TODO AI chat"; //TODO AI chat if text non command or unplaned


    }

}
