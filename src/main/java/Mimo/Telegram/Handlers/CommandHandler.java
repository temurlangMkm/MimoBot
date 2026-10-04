package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.UserService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class CommandHandler {

    private final UserService userService;

    public CommandHandler( UserService userService) {
        this.userService = userService;
    }



    public String handle(String message, Long chatId) {

        String[] command = message.split(" ");

        return switch (command[0]) {
            case "/get" -> getHandle(message, chatId);
            case "/set" -> userService.set(chatId);
            case "/lang" -> userService.setLang(message, chatId);
            case "/notify" -> userService.setNotify(message, chatId);
            case "/forgetMe" -> userService.deleteUser(chatId);
            case "/me" -> userService.infoAboutUser(chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }

    public String getHandle(String message, Long chatId){

        String[] command = message.split(" ");

        if(command.length == 1) {
            return userService
                    .getWeeKSchedule(chatId);
        }


        return switch (command[1].toLowerCase()) {
            case "dushanba", "monday", "понедельник" -> userService.getDaySchedule(DayOfWeek.MONDAY,chatId);
            case "seshanba", "tuesday", "вторник" -> userService.getDaySchedule(DayOfWeek.TUESDAY,chatId);
            case "chorshanba", "wednesday", "среда" -> userService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId);
            case "payshanba", "thursday", "четверг" -> userService.getDaySchedule(DayOfWeek.THURSDAY,chatId);
            case "juma", "friday", "пятница" -> userService.getDaySchedule(DayOfWeek.FRIDAY,chatId);
            case "shanba", "saturday", "суббота" -> userService.getDaySchedule(DayOfWeek.SATURDAY,chatId);
            case "yakshhanba", "sunday", "воскресения" -> userService.getDaySchedule(DayOfWeek.SUNDAY,chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }
}
