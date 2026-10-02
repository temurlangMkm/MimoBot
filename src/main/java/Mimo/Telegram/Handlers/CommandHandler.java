package Mimo.Telegram.Handlers;

import Mimo.Telegram.TelegramService;
import org.springframework.stereotype.Service;

@Service
public class CommandHandler {

    private final TelegramService service;

    public CommandHandler(TelegramService service) {
        this.service = service;
    }



    public String handle(String message, Long chatId) {

        String command = message.split(" ")[0];

        return switch (command) {
            case "/get" -> service.getSchedule(message, chatId);
            case "/set" -> service.setSchedule(message, chatId);
            case "/lang" -> service.setLang(message, chatId);
            case "/notify" -> service.setNotify(message, chatId);
            case "/forgetMe" -> service.deleteUser(chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }
}
