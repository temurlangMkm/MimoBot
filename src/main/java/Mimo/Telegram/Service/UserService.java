package Mimo.Telegram.Service;

import Mimo.Schedule.Service.GroupService;
import Mimo.Schedule.Service.ScheduleService;
import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Language;
import Mimo.Telegram.Repository.UsersRepository;
import Mimo.Telegram.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public String infoAboutUser(Long id){ //forTest

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
        userToSave.setLanguage(String.valueOf(Language.UZ));
        userToSave.setState(State.CHOICE_GROUP);
        userRepo.save(userToSave);

        return "You are new in this bot, please send your group";

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
        user.setState(State.NONE);
        userRepo.save(user);

        return text;
    }

    public String setNotify(String message, Long chatId) {
        return "TODO";
    }

    public String setLang(String message, Long chatId) {
        return "TODO";
    }

    @Transactional
    public String deleteUser(Long chatId) {
        userRepo.deleteByTgId(chatId);
        return "User "+chatId+" deleted.";
    }


    public String getSchedule(String message, Long chatId) {
        UserEntity user = userRepo.findByTgId(chatId);
        if(user==null) return registration(chatId);
        return "TODO";
    }

    public String set (Long id){
        UserEntity user = userRepo.findByTgId(id);

        if(user == null){
            return registration(id);
        }

        user.setState(State.SEND_SCHEDULE);
        userRepo.save(user);
        return "Send Your Schedule in format JSON";
    }

}
