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
            ORDER BY
                CASE s.dayOfWeek
                    WHEN 'MONDAY' THEN 1
                    WHEN 'TUESDAY' THEN 2
                    WHEN 'WEDNESDAY' THEN 3
                    WHEN 'THURSDAY' THEN 4
                    WHEN 'FRIDAY' THEN 5
                    WHEN 'SATURDAY' THEN 6
                    WHEN 'SUNDAY' THEN 7
                END,
                    s.startTime
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
            DayOfWeek dayOfWeek
    );

}
