package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.OldUserService;
import Mimo.Telegram.State;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Locale;

@Service
public class ChatHandler {

    private final CommandHandler commandHandler;
    private final StateHandler stateHandler;
    private final OldUserService oldUserService;

    public ChatHandler(CommandHandler commandHandler, StateHandler stateHandler, OldUserService oldUserService){
        this.commandHandler = commandHandler;
        this.stateHandler = stateHandler;
        this.oldUserService = oldUserService;
    }

    //TODO Eliminate several database queries for looking up users.
    public String handle (String message, Long chatId){

        if (message.startsWith("/")) {
            return commandHandler.handle(message, chatId);
        }

        State userState = oldUserService.getState(chatId);

        if(userState != State.NONE) {
            return stateHandler.handle(message, chatId, userState);
        }

        //TODO: Change this and a separate  RegularWordsHanndler
        switch (message.toLowerCase(Locale.ROOT)) {
            case "dushanba", "monday", "понедельник" -> {
                return oldUserService.getDaySchedule(DayOfWeek.MONDAY,chatId);
            }
            case "seshanba", "tuesday", "вторник" -> {
                return oldUserService.getDaySchedule(DayOfWeek.TUESDAY,chatId);
            }
            case "chorshanba", "wednesday", "среда" -> {
                return oldUserService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId);
            }
            case "payshanba", "thursday", "четверг" -> {
                return oldUserService.getDaySchedule(DayOfWeek.THURSDAY,chatId);
            }
            case "juma", "friday", "пятница" -> {
                return oldUserService.getDaySchedule(DayOfWeek.FRIDAY,chatId);
            }
            case "shanba", "saturday", "суббота" -> {
                return oldUserService.getDaySchedule(DayOfWeek.SATURDAY,chatId);
            }
            case "yakshhanba", "sunday", "воскресения" -> {
                return oldUserService.getDaySchedule(DayOfWeek.SUNDAY,chatId);
            }
        };

        return ""; //TODO AI chat if text non command or unplaned


    }

}
