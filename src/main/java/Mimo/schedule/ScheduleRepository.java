package Mimo.schedule;

import Mimo.schedule.Entitys.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

}
