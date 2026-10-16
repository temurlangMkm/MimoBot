package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.ChatService;
import Mimo.Telegram.Service.UserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Locale;

@Service
public class ChatHandler {

    private final CommandHandler commandHandler;
    private final StateHandler stateHandler;
    private final UserService userService;
    private final ChatService chatService;

    public ChatHandler(CommandHandler commandHandler, StateHandler stateHandler, UserService userService, ChatService chatService){
        this.commandHandler = commandHandler;
        this.stateHandler = stateHandler;
        this.userService = userService;
        this.chatService = chatService;
    }


    public String handle (String message, Long chatId){

        if (message.startsWith("/")) {
            return commandHandler.handle(message, chatId);
        }

        if(chatId>0){
            State state = userService.getState(chatId);
        }
        State state = chatService.getState(chatId);


        if(state != State.NONE) {
            return stateHandler.handle(message, chatId, state);
        }

        switch (message.toLowerCase(Locale.ROOT)) {
            case "dushanba", "monday", "понедельник" -> {
               return chatId > 0 ? userService.getDaySchedule(DayOfWeek.MONDAY,chatId) : chatService.getDaySchedule(DayOfWeek.MONDAY,chatId) ;
            }
            case "seshanba", "tuesday", "вторник" -> {
                return chatId > 0 ? userService.getDaySchedule(DayOfWeek.TUESDAY,chatId) : chatService.getDaySchedule(DayOfWeek.TUESDAY,chatId);
            }
            case "chorshanba", "wednesday", "среда" -> {
                return chatId > 0 ? userService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId) : chatService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId);
            }
            case "payshanba", "thursday", "четверг" -> {
                return chatId > 0 ? userService.getDaySchedule(DayOfWeek.THURSDAY,chatId) : chatService.getDaySchedule(DayOfWeek.THURSDAY,chatId);
            }
            case "juma", "friday", "пятница" -> {
                return chatId > 0 ? userService.getDaySchedule(DayOfWeek.FRIDAY,chatId) : chatService.getDaySchedule(DayOfWeek.FRIDAY,chatId);
            }
            case "shanba", "saturday", "суббота" -> {
                return chatId > 0 ? userService.getDaySchedule(DayOfWeek.SATURDAY,chatId) : chatService.getDaySchedule(DayOfWeek.SATURDAY,chatId);
            }
            case "yakshhanba", "sunday", "воскресения" -> {
                return chatId > 0 ?  userService.getDaySchedule(DayOfWeek.SUNDAY,chatId) : chatService.getDaySchedule(DayOfWeek.SUNDAY,chatId);
            }
        };

        return "";


    }

}
