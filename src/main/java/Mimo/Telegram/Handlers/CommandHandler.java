package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.OldUserService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class CommandHandler {

    private final OldUserService oldUserService;

    public CommandHandler( OldUserService oldUserService) {
        this.oldUserService = oldUserService;
    }



    public String handle(String message, Long chatId) {

        String[] command = message.split("\\s+");

        return switch (command[0]) {
            case "/get" -> getDayHandle(message, chatId);
            case "/setCommand" -> oldUserService.setCommand(chatId);
            case "/forgetMe" -> oldUserService.deleteUser(chatId);
            case "/me" -> oldUserService.infoAboutUser(chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }

    public String getDayHandle(String message, Long chatId){

        String[] command = message.split(" ");

        if(command.length == 1) {
            return oldUserService
                    .getWeeKSchedule(chatId);
        }


        return switch (command[1].toLowerCase()) {
            case "dushanba", "monday", "понедельник" -> oldUserService.getDaySchedule(DayOfWeek.MONDAY,chatId);
            case "seshanba", "tuesday", "вторник" -> oldUserService.getDaySchedule(DayOfWeek.TUESDAY,chatId);
            case "chorshanba", "wednesday", "среда" -> oldUserService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId);
            case "payshanba", "thursday", "четверг" -> oldUserService.getDaySchedule(DayOfWeek.THURSDAY,chatId);
            case "juma", "friday", "пятница" -> oldUserService.getDaySchedule(DayOfWeek.FRIDAY,chatId);
            case "shanba", "saturday", "суббота" -> oldUserService.getDaySchedule(DayOfWeek.SATURDAY,chatId);
            case "yakshhanba", "sunday", "воскресения" -> oldUserService.getDaySchedule(DayOfWeek.SUNDAY,chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }
}
