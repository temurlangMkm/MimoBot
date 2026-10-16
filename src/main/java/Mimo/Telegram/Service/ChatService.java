package Mimo.Telegram.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.ChatEntity;
import Mimo.Telegram.Language;
import Mimo.Telegram.Repository.ChatsRepository;
import Mimo.Telegram.State;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class ChatService {

    private final ChatsRepository chatRepo;
    private final GroupService  groupService;
    private final ScheduleService scheduleService;

    public ChatService(ChatsRepository chatRepo, GroupService groupService, ScheduleService scheduleService) {
        this.chatRepo = chatRepo;
        this.groupService = groupService;
        this.scheduleService = scheduleService;
    }

    public String infoAboutChat(Long chatId){

        ChatEntity chat = chatRepo.findByTgId(chatId);

        if( chat == null){
            return registration(chatId);
        }

        return "group: "+ groupService.getNameByID(chat.getGroupID())+"\nState: "+chat.getState();

    }

    public State getState(Long chatId){

        ChatEntity chat = chatRepo.findByTgId(chatId);

        if(chat == null){
            return State.UNREGISTERED;
        }

        return chat.getState();
    }

    @Transactional
    public String registration(Long chatId){

        ChatEntity chatToSave = new ChatEntity();
        chatToSave.setTgId(chatId);
        chatToSave.setGroupID(null);
        chatToSave.setNotifications(false);
        chatToSave.setOnlyCommand(true);
        chatToSave.setLanguage(Language.UZ);
        chatToSave.setState(State.CHOICE_GROUP);
        chatRepo.save(chatToSave);

        return "You are new in this bot, please send your group.";

    }

    public String saveGroup(Long chatId, String text){

        Long groupId = groupService.saveGroup(text);

        if(groupId>0){
            ChatEntity chat = chatRepo.findByTgId(chatId);
            chat.setState(State.NONE);
            chat.setGroupID(groupId);
            chatRepo.save(chat);
            return "Group Saved";
        }else{
            return "Invalid group name. Try again";
        }
    }

    @Transactional
    public String deleteUser(Long chatId) {
        chatRepo.deleteByTgId(chatId);
        return "User "+chatId+" deleted from BD.";
    }

    public String getWeeKSchedule(Long chatId) {

        ChatEntity chat = chatRepo.findByTgId(chatId);

        if(chat == null || chat.getGroupID()==null){
            return registration(chatId);
        }

        Long groupId = chat.getGroupID();
        List<Schedule> scheduleList =
                scheduleService.getScheduleForWeek(groupId);

        if (scheduleList.isEmpty()) {
            return "На этой неделе занятий нет.";
        }

        StringBuilder text = new StringBuilder();

        DayOfWeek currentDay = null;

        for (Schedule schedule : scheduleList) {

            if (currentDay != schedule.getDayOfWeek()) {
                currentDay = schedule.getDayOfWeek();

                text.append("📅")
                        .append(getDayName(currentDay, chat.getLanguage()))
                        .append("\n\n");
            }

            text.append(schedule.getStartTime())
                    .append(" — ")
                    .append(schedule.getSubject())
                    .append("\n");

            text.append(schedule.getType())
                    .append(" · ")
                    .append(schedule.getRoom())
                    .append("\n");

            text.append(schedule.getTeacher())
                    .append("\n\n");
        }

        return text.toString().trim();
    }

    public String getDaySchedule(DayOfWeek dayOfWeek, Long chatId) {

        ChatEntity chat = chatRepo.findByTgId(chatId);

        if(chat == null || chat.getGroupID()==null){
            return registration(chatId);
        }

        Long groupId = chat.getGroupID();

        List<Schedule> scheduleList =
                scheduleService.getScheduleForDay(groupId, dayOfWeek);

        if (scheduleList.isEmpty()) {
            return "На этот день занятий нет.";
        }

        StringBuilder text = new StringBuilder();

        text.append("📅")
                .append(getDayName(dayOfWeek, chat.getLanguage()))
                .append("\n\n");

        for (Schedule schedule : scheduleList) {

            text.append(schedule.getStartTime())
                    .append(" — ")
                    .append(schedule.getSubject())
                    .append("\n");

            text.append(schedule.getType())
                    .append(" · ")
                    .append(schedule.getRoom())
                    .append("\n");

            text.append(schedule.getTeacher())
                    .append("\n\n");
        }

        return text.toString().trim();
    }

    private String getDayName(DayOfWeek dayOfWeek, @NonNull Language lan) {
        String dayName;
        if(lan.equals(Language.RU)){
            dayName = switch (dayOfWeek) {
                case MONDAY -> "Понедельник";
                case TUESDAY -> "Вторник";
                case WEDNESDAY -> "Среда";
                case THURSDAY -> "Четверг";
                case FRIDAY -> "Пятница";
                case SATURDAY -> "Суббота";
                case SUNDAY -> "Воскресенье";
            };
            return dayName;
        }

        if(lan.equals(Language.UZ)){
            dayName = switch (dayOfWeek) {
                case MONDAY -> "Dushanba";
                case TUESDAY -> "Seshanba";
                case WEDNESDAY -> "Chorshanba";
                case THURSDAY -> "Payshanba";
                case FRIDAY -> "Juma";
                case SATURDAY -> "Shanba";
                case SUNDAY -> "Yakshanba";
            };
            return dayName;
        }

        return dayOfWeek.name();

    }
}

