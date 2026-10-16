package Mimo.Telegram.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.MessageToSend;
import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Repository.TgUsersRepository;
import Mimo.Telegram.State;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final TgUsersRepository userRepo;
    private final GroupService groupService;
    private final ScheduleService scheduleService;
    private final ScheduleFormatter scheduleFormatter;

    public MessageToSend newTgUser(Long chatId) {
        UserEntity userToSave = new UserEntity();
        userToSave.setTgId(chatId);
        userToSave.setNotifications(false);
        userToSave.setOnlyCommand(true);
        userToSave.setLanguage(Language.UZ);
        userToSave.setState(State.CHOICE_GROUP);
        userRepo.save(userToSave);

        return MessageToSend.builder().messageKey(MessageKey.WELCOME).id(chatId).build();
    }

    public MessageToSend choiceGroup(@NonNull UserEntity user) {
        user.setState(State.CHOICE_GROUP);
        userRepo.save(user);
        return MessageToSend.builder().messageKey(MessageKey.CHOOSE_GROUP).id(user.getTgId()).build();
    }

    public MessageToSend setGroup (UserEntity user, String groupName){

        Long id =  groupService.saveGroup(groupName);

        if(id<0){
            return MessageToSend.builder().messageKey(MessageKey.BAD_GROUP_NAME).id(user.getTgId()).build();
        }

        user.setGroupId(id);
        user.setState(State.NONE);
        userRepo.save(user);
        return MessageToSend.builder().messageKey(MessageKey.GROUP_SELECTED).id(user.getTgId()).build();

    }

    public MessageToSend infoAboutChat(@NonNull UserEntity user){

        return MessageToSend.builder()
                .messageKey(MessageKey.ABOUT_ME)
                .text("group: "+groupService.getNameById(user.getGroupId())+"\nState: " + user.getState())
                .id(user.getTgId())
                .build();

    }

    public MessageToSend getWeeKSchedule(@NonNull UserEntity user) {
        Long groupId = user.getGroupId();
        if (groupId == null || groupId < 0) return choiceGroup(user);

        List<Schedule> scheduleList = scheduleService.getScheduleForWeek(groupId);
        if (scheduleList.isEmpty()) {
            return MessageToSend.builder().messageKey(MessageKey.SCHEDULE_NOT_FOUND).id(user.getTgId()).build();
        }

        String formattedText = scheduleFormatter.formatWeek(scheduleList);
        return MessageToSend.builder()
                .messageKey(MessageKey.SCHEDULE_FOR_WEEK)
                .text(formattedText)
                .id(user.getTgId())
                .build();
    }

    public MessageToSend getDaySchedule(DayOfWeek dayOfWeek, @NonNull UserEntity user) {


        Long groupId = user.getGroupId();
        if (groupId == null || groupId < 0) return choiceGroup(user);

        List<Schedule> scheduleList = scheduleService.getScheduleForDay(groupId, dayOfWeek);
        if (scheduleList.isEmpty()) {
            return MessageToSend.builder().messageKey(MessageKey.SCHEDULE_NOT_FOUND).id(user.getId()).build();
        }

        String formattedText = scheduleFormatter.formatWeek(scheduleList);
        return MessageToSend.builder()
                .messageKey(MessageKey.SCHEDULE_FOR_WEEK)
                .text(formattedText)
                .id(user.getId())
                .build();
    }

    public MessageToSend setSchedule(@NonNull UserEntity user){

        user.setState(State.SEND_SCHEDULE);
        userRepo.save(user);
        return MessageToSend.builder()
                .messageKey(MessageKey.SEND_SCHEDULE)
                .id(user.getTgId())
                .build();
    }

    public MessageToSend saveSchedule(@NonNull UserEntity user, String json){

        String groupName = groupService.getNameById(user.getGroupId());

        String text =  scheduleService.setSchedule(json, groupName);

        if(text.startsWith("ERROR")){
            System.out.println(text);
            return MessageToSend.builder()
                    .messageKey(MessageKey.BAD_JSON)
                    .text("")
                    .id(user.getTgId())
                    .build();
        }

        user.setState(State.NONE);
        userRepo.save(user);

        return getWeeKSchedule(user);
    }

}
