package Mimo.Telegram.Handlers;

import Mimo.Telegram.Service.ChatService;
import Mimo.Telegram.Service.UserService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class CommandHandler {

    private final UserService userService;
    private final ChatService chatService;

    public CommandHandler(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }



    public String handle(String message, Long chatId) {

        String[] command = message.split("\\s+");

        return switch (command[0]) {
            case "/get" -> getHandle(message, chatId);
            case "/setCommand" -> chatId > 0 ? userService.setCommand(chatId) : "Bunday buyruq mavjud emas.";
            case "/forgetMe" -> chatId > 0 ? userService.deleteUser(chatId) : chatService.deleteUser(chatId);
            case "/me" -> chatId > 0 ? userService.infoAboutUser(chatId) : chatService.infoAboutChat(chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }

    public String getHandle(String message, Long chatId){

        String[] command = message.split(" ");

        if(command.length == 1) {
            return chatId > 0 ?
                    userService.getWeeKSchedule(chatId)
                    : chatService.getWeeKSchedule(chatId) ;
        }


        return switch (command[1].toLowerCase()) {
            case "dushanba", "monday", "понедельник" -> chatId > 0 ? userService.getDaySchedule(DayOfWeek.MONDAY,chatId) : chatService.getDaySchedule(DayOfWeek.MONDAY,chatId) ;
            case "seshanba", "tuesday", "вторник" -> chatId > 0 ? userService.getDaySchedule(DayOfWeek.TUESDAY,chatId) : chatService.getDaySchedule(DayOfWeek.TUESDAY,chatId);
            case "chorshanba", "wednesday", "среда" -> chatId > 0 ? userService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId) : chatService.getDaySchedule(DayOfWeek.WEDNESDAY,chatId);
            case "payshanba", "thursday", "четверг" -> chatId > 0 ? userService.getDaySchedule(DayOfWeek.THURSDAY,chatId) : chatService.getDaySchedule(DayOfWeek.THURSDAY,chatId);
            case "juma", "friday", "пятница" -> chatId > 0 ? userService.getDaySchedule(DayOfWeek.FRIDAY,chatId) : chatService.getDaySchedule(DayOfWeek.FRIDAY,chatId);
            case "shanba", "saturday", "суббота" -> chatId > 0 ? userService.getDaySchedule(DayOfWeek.SATURDAY,chatId) : chatService.getDaySchedule(DayOfWeek.SATURDAY,chatId);
            case "yakshhanba", "sunday", "воскресения" -> chatId > 0 ?  userService.getDaySchedule(DayOfWeek.SUNDAY,chatId) : chatService.getDaySchedule(DayOfWeek.SUNDAY,chatId);
            default -> "Bunday buyruq mavjud emas.";
        };
    }
}
