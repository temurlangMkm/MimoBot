package Mimo.Telegram;


import Mimo.Schedule.Entity.GroupEntity;
import Mimo.Schedule.Repository.GroupRepository;
import Mimo.Telegram.Entity.UserEntity;
import Mimo.Telegram.Repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class TelegramService {

    private final UsersRepository userRepo;
    private final GroupRepository groupRepo;

    public TelegramService(UsersRepository userRepo, GroupRepository groupRepository) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepository;
    }


    public String stateManager(String message, Long chatId){


        UserEntity user = userRepo.findByTgId(chatId);

        if(user==null) return registration(chatId);

        State state = user.getState();

        String text = "";

        switch (state){
            case NONE -> text = "";
            case CHOICE_GROUP -> text = choiceGroup(message, user);
        };
        
        return text;
    }

    @Transactional
    private String choiceGroup(String message, UserEntity user) {
        String normalized = message
                .replaceAll("[\\s-]", "")
                .toUpperCase(Locale.ROOT);
        if (normalized.matches("[A-ZA-ЯЁ]\\d{4}")) {
            String groupName = normalized.charAt(0)
                    +  "-"  +
                    normalized.substring(1, 3)
                    +  "-"  +
                    normalized.substring(3, 5);

            Long groupId;

            GroupEntity groupEntity = groupRepo.findByName(groupName);

            if(groupEntity == null){

                GroupEntity groupToSave = new GroupEntity();
                groupToSave.setName(groupName);
                groupRepo.save(groupToSave);

                GroupEntity group = groupRepo.findByName(groupName);
                groupId = group.getId();
            }else{
                groupId = groupEntity.getId();
            }

            user.setState(State.NONE);
            user.setGroupID(groupId);
            userRepo.save(user);

            return "Saqlandi. Sizninig guruxingiz "+groupName;
        }

        return "Gurux Xatto kiritilgan. namuna K-32-24.";

    }

    public String getSchedule(String message, Long chatId) {

        if(!userRepo.existsByTgId(chatId)){
            return registration(chatId);
        }

        UserEntity user = userRepo.findByTgId(chatId);

        if(user.getGroupID() == null){
            user.setState(State.CHOICE_GROUP);
            userRepo.save(user);
            return "Siz gurux talamagansiz. guruxingizni aytinng.";
        }

        return "TODO";
    }

    public String setSchedule(String message, Long chatId) {
        return "TODO";
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
        return "Bajarildi.";
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
