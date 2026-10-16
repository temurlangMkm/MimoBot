package Mimo.Telegram.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Telegram.Entity.UserEntity;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.util.List;

@Component
public class ScheduleFormatter {

    MessageService messageService;

    public String formatWeek(List<Schedule> scheduleList) {
        StringBuilder text = new StringBuilder();
        DayOfWeek currentDay = null;

        for (Schedule schedule : scheduleList) {
            if (currentDay != schedule.getDayOfWeek()) {
                currentDay = schedule.getDayOfWeek();
                text.append("📅 ").append(getDayName(currentDay)).append("\n\n");
            }
            text.append(formatSingleLesson(schedule));
        }
        return text.toString().trim();
    }

    public String formatDay(List<Schedule> scheduleList, DayOfWeek dayOfWeek) {
        StringBuilder text = new StringBuilder();
        text.append("📅 ").append(getDayName(dayOfWeek)).append("\n\n");

        for (Schedule schedule : scheduleList) {
            text.append(formatSingleLesson(schedule));
        }
        return text.toString().trim();
    }

    private String formatSingleLesson(Schedule schedule) {
        return schedule.getStartTime() + " — " + schedule.getSubject() + "\n" +
                schedule.getType() + " · " + schedule.getRoom() + "\n" +
                schedule.getTeacher() + "\n\n";
    }

    private String getDayName(DayOfWeek dayOfWeek) {
        return "{"+dayOfWeek.name()+"}";
    }
}
