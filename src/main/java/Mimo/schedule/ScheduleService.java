package Mimo.schedule;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class ScheduleService {


    public List<Schedule> getScheduleByDayOfWeekAndGroupId(
            Long user_id,
            DayOfWeek dayOfWeek
    ){
        return null;
    }

    public List<Schedule> getScheduleForWeekByGroupId(
            Long group_id){
        return null;
    }
}
