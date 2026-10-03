package Mimo.Schedule.Repository;

import Mimo.Schedule.Entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    boolean existsByName(String groupName);
    GroupEntity findByName(String groupName);

}

