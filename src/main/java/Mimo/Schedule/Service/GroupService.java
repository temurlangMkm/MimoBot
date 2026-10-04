package Mimo.Schedule.Service;

import Mimo.Schedule.Entity.GroupEntity;
import Mimo.Schedule.Repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }



    public Long saveGroup(String text){

        String groupName = normalizeGroup(text);

        Long id;

        if(groupRepository.existsByName(groupName)){
            return groupRepository.findByName(groupName).getId();
        }

        if(groupName.equals("Invalid group name")){
            return -1L;
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupName);

        return  groupRepository.save(groupEntity).getId();
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

    public Long getIdByName(String name){
        return groupRepository.findByName(name).getId();
    }

    public String getNameById(Long groupId) {

        GroupEntity group = groupRepository.findById(groupId).orElse(null);

        return group==null ? "null":group.getName();
    }
}
