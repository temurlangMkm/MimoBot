package Mimo.Schedule.Service;

import Mimo.Schedule.Entity.GroupEntity;
import Mimo.Schedule.Repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GroupService {

    GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }



    public String saveGroup(String text){

        String groupName = normalizeGroup(text);

        if(groupRepository.existsByName(groupName)){
            return "This group already exists.";
        }

        if(groupName.equals("Invalid group name")){
            return "Invalid group name";
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupName);
        groupRepository.save(groupEntity);

        return "Group saved.";
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
