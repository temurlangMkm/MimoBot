package Mimo.Schedule.Service;

import Mimo.Schedule.Entity.GroupEntity;
import Mimo.Schedule.Repository.GroupRepository;
import Mimo.Telegram.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }



    public boolean saveGroup(String text){

        String groupName = normalizeGroup(text);

        if(groupRepository.existsByName(groupName)){
            return true;
        }

        if(groupName.equals("Invalid group name")){
            return false;
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupName);
        groupRepository.save(groupEntity);

        return true;
    }

    public String normalizeGroup(String text) {

        String cleaned = text
                .replaceAll("[\\s-]", "")
                .toUpperCase(Locale.ROOT);

        if (!cleaned.matches("[A-ZA-ЯЁ]\\d{4}")) {
                   return  "Invalid group name";
        }

        return cleaned.charAt(0)
                + "-" +
                cleaned.substring(1, 3)
                + "-" +
                cleaned.substring(3, 5);
    }

    public String getNameByID(Long id){

        GroupEntity group = groupRepository.findById(id).orElse(null);

        if(group==null){
            return "No such group was found.";
        }

        return group.getName();
    }
}
