package Mimo.Telegram.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.ChatEntity;
import Mimo.Telegram.Entity.MessageToSend;
import Mimo.Telegram.Repository.TgChatsRepository;
import Mimo.Telegram.Repository.TgUsersRepository;
import Mimo.Telegram.State;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;


@Service
public class ChatService {

    private final TgChatsRepository chatRepo;
    private final TgUsersRepository userRepo;
    private final GroupService groupService;
    private final ScheduleService scheduleService;

    public ChatService(TgChatsRepository chatRepo, TgUsersRepository userRepo, GroupService groupService, ScheduleService scheduleService) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.groupService = groupService;
        this.scheduleService = scheduleService;
    }

    public MessageToSend newTgChat(Long chatId){

        ChatEntity newChat = new ChatEntity();
        newChat.setId(chatId);
        newChat.setState(State.CHOICE_GROUP);
        newChat.setGroupId(null);
        newChat.setNotifications(true);
        newChat.setOnlyCommand(false);
        newChat.setLanguage(Language.UZ);
        chatRepo.save(newChat);

        return new MessageToSend(MessageKey.WELCOME, "", chatId);
    }

    public MessageToSend choiceGroup(@NonNull ChatEntity chat){
        chat.setState(State.CHOICE_GROUP);
        chatRepo.save(chat);
        return new MessageToSend(MessageKey.CHOOSE_GROUP, "",  chat.getId());
    }

    public MessageToSend setGroup (ChatEntity chat,  String groupName){

        Long id =  groupService.saveGroup(groupName);

        if(id<0){
            return new MessageToSend(MessageKey.BAD_GROUP_NAME, "", chat.getId());
        }

        chat.setGroupId(id);
        chatRepo.save(chat);
        return new MessageToSend(MessageKey.GROUP_SELECTED, "", chat.getId());

    }

    public MessageToSend infoAboutChat(@NonNull ChatEntity chat){

        return new MessageToSend(MessageKey.TEXT, "group: "+ groupService.getNameByID(chat.getGroupId())+"\nState: "+chat.getState(), chat.getId());

    }

    public MessageToSend getWeeKSchedule(@NonNull ChatEntity chat) {

        Long groupId = chat.getGroupId();

        if(groupId == null || groupId < 0){
            return choiceGroup(chat);
        }

        List<Schedule> scheduleList =
                scheduleService.getScheduleForWeek(groupId);

        if (scheduleList.isEmpty()) {
            return new MessageToSend(MessageKey.SCHEDULE_NOT_FOUND, "", chat.getId());
        }

        StringBuilder text = new StringBuilder();

        DayOfWeek currentDay = null;

        for (Schedule schedule : scheduleList) {

            if (currentDay != schedule.getDayOfWeek()) {
                currentDay = schedule.getDayOfWeek();

                text.append("📅")
                        .append(getDayName(currentDay))
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

        return new MessageToSend(MessageKey.SCHEDULE_FOR_WEEK, text.toString().trim() , chat.getId());
    }

    public  MessageToSend getDaySchedule(DayOfWeek dayOfWeek, @NonNull ChatEntity chat) {


        Long groupId = chat.getGroupId();

        List<Schedule> scheduleList =
                scheduleService.getScheduleForDay(groupId, dayOfWeek);

        if (scheduleList.isEmpty()) {
            return new MessageToSend(MessageKey.SCHEDULE_NOT_FOUND, "", chat.getId());
        }

        StringBuilder text = new StringBuilder();

        text.append("📅")
                .append(getDayName(dayOfWeek))
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

        return new MessageToSend(MessageKey.SCHEDULE_FOR_DAY, text.toString().trim() , chat.getId());
    }

    private String getDayName(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "{MONDAY}";
            case TUESDAY -> "{TUESDAY}";
            case WEDNESDAY -> "{WEDNESDAY}";
            case THURSDAY -> "{THURSDAY}";
            case FRIDAY -> "{FRIDAY }";
            case SATURDAY -> "{SATURDAY}";
            case SUNDAY -> "{SUNDAY}";
        };
    }

}
