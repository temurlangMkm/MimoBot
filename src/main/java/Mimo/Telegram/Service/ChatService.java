package Mimo.Telegram.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.ChatEntity;
import Mimo.Telegram.Entity.MessageToSend;
import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Repository.TgChatsRepository;
import Mimo.Telegram.Repository.TgUsersRepository;
import Mimo.Telegram.State;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final TgChatsRepository chatRepo;
    private final TgUsersRepository userRepo;
    private final GroupService groupService;
    private final ScheduleFormatter scheduleFormatter;
    private final ScheduleService scheduleService;

    public MessageToSend choiceGroup(@NonNull ChatEntity chat){
        chat.setState(State.CHOICE_GROUP);
        chatRepo.save(chat);
        return MessageToSend.builder().messageKey(MessageKey.CHOOSE_GROUP).id(chat.getTgId()).build();
    }

    public MessageToSend setGroup (ChatEntity chat,  String groupName){

        Long id =  groupService.saveGroup(groupName);

        if(id<0){
            return MessageToSend.builder().messageKey(MessageKey.BAD_GROUP_NAME).id(chat.getTgId()).build();
        }

        chat.setGroupId(id);
        chat.setState(State.NONE);
        chatRepo.save(chat);
        return MessageToSend.builder().messageKey(MessageKey.GROUP_SELECTED).id(chat.getTgId()).build();

    }

    public MessageToSend newTgChat(Long chatId) {
        ChatEntity ChatToSave = new ChatEntity();
        ChatToSave.setTgId(chatId);
        ChatToSave.setNotifications(false);
        ChatToSave.setOnlyCommand(true);
        ChatToSave.setLanguage(Language.UZ);
        ChatToSave.setState(State.CHOICE_GROUP);
        chatRepo.save(ChatToSave);

        return MessageToSend.builder().messageKey(MessageKey.WELCOME).id(chatId).build();
    }


    public MessageToSend infoAboutChat(@NonNull ChatEntity chat){

        return MessageToSend.builder().messageKey(MessageKey.ABOUT_ME).text("group: "+groupService.getNameById(chat.getGroupId())+"\nState: "+ chat.getState()).id(chat.getTgId()).build();

    }

    public MessageToSend getWeeKSchedule(@NonNull ChatEntity chat) {
        Long groupId = chat.getGroupId();
        if (groupId == null || groupId < 0) return choiceGroup(chat);

        List<Schedule> scheduleList = scheduleService.getScheduleForWeek(groupId);
        if (scheduleList.isEmpty()) {
            return MessageToSend.builder().messageKey(MessageKey.SCHEDULE_NOT_FOUND).id(chat.getTgId()).build();
        }

        String formattedText = scheduleFormatter.formatWeek(scheduleList);
        return MessageToSend.builder()
                .messageKey(MessageKey.SCHEDULE_FOR_WEEK)
                .text(formattedText)
                .id(chat.getTgId())
                .build();
    }

    public MessageToSend getDaySchedule(DayOfWeek dayOfWeek, @NonNull ChatEntity chat) {


        Long groupId = chat.getGroupId();
        if (groupId == null || groupId < 0) return choiceGroup(chat);

        List<Schedule> scheduleList = scheduleService.getScheduleForDay(groupId, dayOfWeek);
        if (scheduleList.isEmpty()) {
            return MessageToSend.builder().messageKey(MessageKey.SCHEDULE_NOT_FOUND).id(chat.getId()).build();
        }

        String formattedText = scheduleFormatter.formatWeek(scheduleList);
        return MessageToSend.builder()
                .messageKey(MessageKey.SCHEDULE_FOR_WEEK)
                .text(formattedText)
                .id(chat.getTgId())
                .build();
    }

}
