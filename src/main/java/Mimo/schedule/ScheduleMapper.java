package Mimo.schedule;

import Mimo.schedule.Entitys.ScheduleEntity;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ScheduleMapper {




    Schedule toDomain (ScheduleEntity entity){

       DayOfWeek dayOfWeek =  switch (entity.getDayOfWeek()){
           case 1 -> DayOfWeek.MONDAY;
           case 2 -> DayOfWeek.TUESDAY;
           case 3 -> DayOfWeek.WEDNESDAY;
           case 4 -> DayOfWeek.THURSDAY;
           case 5 -> DayOfWeek.FRIDAY;
           case 6 -> DayOfWeek.SATURDAY;
           case 7 -> DayOfWeek.SUNDAY;
           default -> throw new IllegalStateException("The day of week must be between 1 and 7. Your date: " + entity.getDayOfWeek());
       };

        return new Schedule(
                entity.getId(),
                dayOfWeek,
                entity.getGroup().getGroupName(),
                entity.getSubject().getName(),
                entity.getTeacher().getFullName(),
                entity.getRoom().getName(),
                entity.getPeriod(),
                entity.getSubGroup()
        );
    }
}
