package Mimo.Schedule.Repository;

import Mimo.Schedule.Entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findAllByNameIn(Collection<String> subjects);
}
