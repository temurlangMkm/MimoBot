package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.UserService;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {

    private final UserService userService;

    public CommandHandler( UserService userService) {
        this.userService = userService;
    }



    public String handle(String message, Long chatId) {

        String command = message.split(" ")[0];

        return switch (command) {
            case "/get" -> userService.getSchedule(message, chatId);
            case "/set" -> userService.setSchedule(message, chatId);
            case "/lang" -> userService.setLang(message, chatId);
            case "/notify" -> userService.setNotify(message, chatId);
            case "/forgetMe" -> userService.deleteUser(chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }
}
