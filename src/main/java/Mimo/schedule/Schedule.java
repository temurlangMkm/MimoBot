package Mimo.schedule;
import jakarta.validation.constraints.Null;

import java.time.DayOfWeek;


public record Schedule(
        Long id,
        DayOfWeek dayOfWeek,
        String group,
        String subject,
        String teacher,
        String room,
        int period,
        int subGroup
) {
}



