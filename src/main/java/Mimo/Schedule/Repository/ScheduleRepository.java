package Mimo.Schedule.Repository;
import Mimo.Schedule.Entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    void deleteAllByGroupId(Long groupId);

    @Query("""
        SELECT s
        FROM ScheduleEntity s
        WHERE s.groupId = :groupId
            ORDER BY s.dayOfWeek, s.startTime
    """)
    List<ScheduleEntity> findWeekSchedule(Long groupId);

    @Query("""
        SELECT s
        FROM ScheduleEntity s
        WHERE s.groupId = :groupId
          AND s.dayOfWeek = :dayOfWeek
              ORDER BY s.startTime
    """)
    List<ScheduleEntity> findDaySchedule(
            Long groupId,
            String dayOfWeek
    );

}
