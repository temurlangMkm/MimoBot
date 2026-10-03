package Mimo.Telegram.Service;

import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Language;
import Mimo.Telegram.Repository.UsersRepository;
import Mimo.Telegram.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {


    public UsersRepository userRepo;

    public UserService(UsersRepository userRepo) {
        this.userRepo = userRepo;
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


}
