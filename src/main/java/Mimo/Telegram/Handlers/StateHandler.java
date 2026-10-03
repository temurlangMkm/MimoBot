package Mimo.Telegram.Handlers;

import Mimo.Schedule.Service.GroupService;
import Mimo.Telegram.Service.UserService;
import Mimo.Telegram.State;
import Mimo.Telegram.TelegramService;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

@Component
public class StateHandler {

    private final GroupService groupService;
    private final UserService userService;

    public StateHandler(GroupService groupService, UserService userService) {
        this.groupService = groupService;
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
