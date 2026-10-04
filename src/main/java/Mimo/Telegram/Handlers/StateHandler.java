package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.UserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Component;

@Component
public class StateHandler {

    private final UserService userService;

    public StateHandler(UserService userService) {
        this.userService = userService;
    }

    public String handle(String message, Long  id, State state){

        return switch (state) {
            case CHOICE_GROUP -> userService.saveGroup(id, message);
            case UNREGISTRED -> userService.registration(id);
            default -> "Error. Your message could not be processed at this moment.";
        };

    }
}
