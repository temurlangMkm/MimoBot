package Mimo.Telegram.Service;

import Mimo.Schedule.Service.GroupService;
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

    public UserService(UsersRepository userRepo, GroupService groupService) {
        this.userRepo = userRepo;
        this.groupService = groupService;
    }

    public State getState(Long id){
        UserEntity user = userRepo.findByTgId(id);

        if(user == null){
            return State.UNREGISTRED;
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

        return "Siz yangi siz.";

    }

    public String saveGroup(Long id, String text){

        boolean flag = groupService.saveGroup(text);

        if(flag){
            UserEntity user = userRepo.findByTgId(id);
            user.setState(State.NONE);
            userRepo.save(user);
            return "Group Saved";
        }else{
            return "Invalid group name. Try again";
        }
    }




}
