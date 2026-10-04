package Mimo.Schedule.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table(name = "schedules")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "subject_id")
    private  Long subjectId;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "type")
    private String type;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name =  "start_time")
    private LocalTime startTime;

    public ScheduleEntity(){}

    public ScheduleEntity(Long id, Long groupId, Long subjectId, Long teacherId, Long roomId, String type, String dayOfWeek, LocalTime startTime) {
        this.id = id;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.roomId = roomId;
        this.type = type;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
    }
}
