package Mimo.schedule;

import Mimo.schedule.Entitys.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {


    @Query("""
            SELECT s from ScheduleEntity s
                where s.group.id =:groupId
                and s.dayOfWeek =:dayOfWeek
   """)
    List<ScheduleEntity> getScheduleByDayOfWeekAndGroupId (
           @Param("groupId") Long group_id,
           @Param("dayOfWeek") int dayOfWeek               // 1=Monday, 2=Tuesday, ... 7 = Sunday  0<dayOfWeek<8
    );

    @Query("""
            SELECT s from ScheduleEntity s
                where s.group.id =:groupId
                ORDER BY s.dayOfWeek
  """)
    List<ScheduleEntity> getScheduleForWeekByGroupId (
            @Param("groupId") Long group_id
    );

    void deleteByGroup_Id(
            Long group_id
    );

    boolean existsByGroup_Id(
            Long groupId
    );



}
