package Mimo.Schedule.Repository;

import Mimo.Schedule.Entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
    List<TeacherEntity> findAllByNameIn(Collection<String> teachers);
}
