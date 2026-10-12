package Mimo.Telegram.Service;


import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.MessageToSend;
import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Repository.TgUsersRepository;
import Mimo.Telegram.State;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class UserService {

    private final TgUsersRepository userRepo;
    private final GroupService  groupService;
    private final ScheduleService scheduleService;

    public UserService(TgUsersRepository userRepo, GroupService groupService, ScheduleService scheduleService) {
        this.userRepo = userRepo;
        this.groupService = groupService;
        this.scheduleService = scheduleService;
    }

    public MessageToSend infoAboutUser(@NonNull UserEntity user){

        return new MessageToSend(MessageKey.TEXT, "group: "+ groupService.getNameByID(user.getGroupId())+"\nState: "+user.getState(), user.getId());

    }

    public MessageToSend newTgUser(Long chatId){

        UserEntity userToSave = new UserEntity();
        userToSave.setTgId(chatId);
        userToSave.setGroupId(null);
        userToSave.setNotifications(false);
        userToSave.setOnlyCommand(true);
        userToSave.setLanguage(String.valueOf(Language.UZ));
        userToSave.setState(State.CHOICE_GROUP);
        userRepo.save(userToSave);

        return new MessageToSend(MessageKey.WELCOME, "", chatId);

    }

    public MessageToSend choiceGroup(@NonNull UserEntity user){
        user.setState(State.CHOICE_GROUP);
        userRepo.save(user);
        return new MessageToSend(MessageKey.CHOOSE_GROUP, "",  user.getId());
    }

    public MessageToSend saveGroup(UserEntity user, String groupName){

        Long groupId = groupService.saveGroup(groupName);

        if(groupId<0){
            return new MessageToSend(MessageKey.BAD_GROUP_NAME, "", user.getId());
        }else{
            user.setState(State.NONE);
            user.setGroupId(groupId);
            userRepo.save(user);
            return new MessageToSend(MessageKey.GROUP_SELECTED, "", user.getId());
        }
    }

    public MessageToSend setSchedule(String message, @NonNull UserEntity user) {

        String groupName = groupService.getNameById(user.getGroupId());

        String text =  scheduleService.setSchedule(message, groupName);

        if(text.startsWith("ERROR")){
            System.out.println(text);
            return new MessageToSend(MessageKey.BAD_JSON, "", user.getId());
        }

        user.setState(State.NONE);
        userRepo.save(user);

        return getWeeKSchedule(user);
    }

    public MessageToSend setSchedule(@NonNull UserEntity user){

        user.setState(State.SEND_SCHEDULE);
        userRepo.save(user);
        return new MessageToSend(MessageKey.SEND_SCHEDULE, "", user.getId());
    }

    public MessageToSend getWeeKSchedule(@NonNull UserEntity user) {

        Long groupId = user.getGroupId();

        if(groupId == null || groupId < 0){
            return choiceGroup(user);
        }

        List<Schedule> scheduleList =
                scheduleService.getScheduleForWeek(groupId);

        if (scheduleList.isEmpty()) {
            return new MessageToSend(MessageKey.SCHEDULE_NOT_FOUND, "", user.getId());
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

        return new MessageToSend(MessageKey.SCHEDULE_FOR_WEEK, text.toString().trim() , user.getId());
    }

    public  MessageToSend getDaySchedule(DayOfWeek dayOfWeek, @NonNull UserEntity user) {


        Long groupId = user.getGroupId();

        if(groupId == null || groupId < 0){
            return choiceGroup(user);
        }

        List<Schedule> scheduleList =
                scheduleService.getScheduleForDay(groupId, dayOfWeek);

        if (scheduleList.isEmpty()) {
            return new MessageToSend(MessageKey.SCHEDULE_NOT_FOUND, "", user.getId());
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

        return new MessageToSend(MessageKey.SCHEDULE_FOR_DAY, text.toString().trim() , user.getId());
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

