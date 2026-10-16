package Mimo.Telegram.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Language;
import Mimo.Telegram.Repository.UsersRepository;
import Mimo.Telegram.State;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class UserService {

    private final UsersRepository userRepo;
    private final GroupService  groupService;
    private final ScheduleService scheduleService;

    public UserService(UsersRepository userRepo, GroupService groupService, ScheduleService scheduleService) {
        this.userRepo = userRepo;
        this.groupService = groupService;
        this.scheduleService = scheduleService;
    }

    public String infoAboutUser(Long id){

        UserEntity user = userRepo.findByTgId(id);

        if(user == null){
            return registration(id);
        }

        return "group: "+ groupService.getNameByID(user.getGroupID())+"\nState: "+user.getState();

    }

    public State getState(Long id){

        UserEntity user = userRepo.findByTgId(id);

        if(user == null){
            return State.UNREGISTERED;
        }

        return user.getState();
    }

    @Transactional
    public String registration(Long chatId){

        UserEntity userToSave = new UserEntity();
        userToSave.setTgId(chatId);
        userToSave.setGroupID(null);
        userToSave.setNotifications(false);
        userToSave.setOnlyCommand(true);
        userToSave.setLanguage(Language.UZ);
        userToSave.setState(State.CHOICE_GROUP);
        userRepo.save(userToSave);

        return "You are new in this bot, please send your group.";

    }

    public String saveGroup(Long id, String text){

        Long groupId = groupService.saveGroup(text);

        if(groupId>0){
            UserEntity user = userRepo.findByTgId(id);
            user.setState(State.NONE);
            user.setGroupID(groupId);
            userRepo.save(user);
            return "Group Saved";
        }else{
            return "Invalid group name. Try again";
        }
    }

    public String setSchedule(String message, Long chatId) {

        UserEntity user = userRepo.findByTgId(chatId);

        String groupName = groupService.getNameById(user.getGroupID());

        String text =  scheduleService.setSchedule(message, groupName);

        if(text.startsWith("ERROR")){
            System.out.println(text); //TODO add logger
            return "invalid JSON";
        }

        user.setState(State.NONE);
        userRepo.save(user);

        return getWeeKSchedule(chatId);
    }

    @Transactional
    public String deleteUser(Long chatId) {
        userRepo.deleteByTgId(chatId);
        return "User "+chatId+" deleted.";
    }

    public String setCommand(Long id){
        UserEntity user = userRepo.findByTgId(id);

        if(user == null){
            return registration(id);
        }

        user.setState(State.SEND_SCHEDULE);
        userRepo.save(user);
        return "Send Your Schedule in format JSON";
    }

    public String getWeeKSchedule(Long id) {

        UserEntity user = userRepo.findByTgId(id);

        if(user == null || user.getGroupID()==null){
            return registration(id);
        }

        Long groupId = user.getGroupID();
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
                        .append(getDayName(currentDay, user.getLanguage()))
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

    public String getDaySchedule(DayOfWeek dayOfWeek, Long id) {

        UserEntity user = userRepo.findByTgId(id);

        if(user == null || user.getGroupID()==null){
            return registration(id);
        }

        Long groupId = user.getGroupID();

        List<Schedule> scheduleList =
                scheduleService.getScheduleForDay(groupId, dayOfWeek);

        if (scheduleList.isEmpty()) {
            return "На этот день занятий нет.";
        }

        StringBuilder text = new StringBuilder();

        text.append("📅")
                .append(getDayName(dayOfWeek, user.getLanguage()))
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

    private @NonNull String getDayName(DayOfWeek dayOfWeek, Language lan) {
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
