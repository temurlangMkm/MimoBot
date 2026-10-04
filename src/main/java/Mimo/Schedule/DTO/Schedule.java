package Mimo.Schedule.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
public class Schedule {

        @JsonProperty("day")
        private DayOfWeek dayOfWeek;
        private String group;
        private String subject;
        private String  type;
        private String room;
        private String  teacher;
        @JsonProperty("time")
        private LocalTime startTime;

        public Schedule(){}

        public Schedule(DayOfWeek dayOfWeek, String group, String subject, String type, String room, String teacher, LocalTime startTime) {
                this.dayOfWeek = dayOfWeek;
                this.group = group;
                this.subject = subject;
                this.type = type;
                this.room = room;
                this.teacher = teacher;
                this.startTime = startTime;
        }

        @Override
        public String toString() {
                return MessageFormat.format("dayOfWeek: {0}  group: {1} subject: {2} time: {3}", dayOfWeek, group, subject, startTime);
        }
}
