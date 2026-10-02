package Mimo.Telegram.Handlers;

import Mimo.Telegram.Repository.UsersRepository;
import Mimo.Telegram.TelegramService;
import org.springframework.stereotype.Service;

@Service
public class ChatHandler {

    private final TelegramService service;
    private final CommandHandler commandHandler;

    public ChatHandler(TelegramService service, CommandHandler commandHandler){
        this.service = service;
        this.commandHandler = commandHandler;
    }

    public String handle (String message, Long chatId){

        if (message.startsWith("/")) {
            return commandHandler.handle(message, chatId);
        }

        return service.stateManager(message, chatId);

    }

}
