package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.OldUserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Component;

@Component
public class StateHandler {

    private final OldUserService oldUserService;

    public StateHandler(OldUserService oldUserService) {
        this.oldUserService = oldUserService;
    }

    public String handle(String message, Long  id, State state){

        return switch (state) {
            case CHOICE_GROUP -> oldUserService.saveGroup(id, message);
            case UNREGISTERED -> oldUserService.registration(id);
            case SEND_SCHEDULE -> oldUserService.setSchedule(message, id);
            default -> "Error. Your message could not be processed at this moment.";
        };

    }
}
