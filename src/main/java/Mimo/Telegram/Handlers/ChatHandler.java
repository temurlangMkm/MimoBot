package Mimo.Telegram.Handlers;

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

    public ChatHandler(CommandHandler commandHandler, StateHandler stateHandler, UserService userService){
        this.commandHandler = commandHandler;
        this.stateHandler = stateHandler;
        this.userService = userService;
    }

    //TODO Eliminate several database queries for looking up users.
    public String handle (String message, Long chatId){

        if (message.startsWith("/")) {
            return commandHandler.handle(message, chatId);
        }

        //TODO: Change this and a separate  RegularWordsHanndler
        switch (message.toLowerCase(Locale.ROOT)) {
            case "dushanba", "monday", "понедельник" -> {
                return userService.getDaySchedule(DayOfWeek.MONDAY,chatId);
            }
            case "seshanba", "tuesday", "вторник" -> {
                return userService.getDaySchedule(DayOfWeek.TUESDAY,chatId);
            }
            case "chorshanba", "wednesday", "среда" -> {
                return userService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId);
            }
            case "payshanba", "thursday", "четверг" -> {
                return userService.getDaySchedule(DayOfWeek.THURSDAY,chatId);
            }
            case "juma", "friday", "пятница" -> {
                return userService.getDaySchedule(DayOfWeek.FRIDAY,chatId);
            }
            case "shanba", "saturday", "суббота" -> {
                return userService.getDaySchedule(DayOfWeek.SATURDAY,chatId);
            }
            case "yakshhanba", "sunday", "воскресения" -> {
                return userService.getDaySchedule(DayOfWeek.SUNDAY,chatId);
            }
        };

        State userState = userService.getState(chatId);

        if(userState != State.NONE) {
            return stateHandler.handle(message, chatId, userState);
        }

        return ""; //TODO AI chat if text non command or unplaned


    }

}
