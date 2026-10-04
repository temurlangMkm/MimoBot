package Mimo.Schedule.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Entity.*;
import Mimo.Schedule.Repository.GroupRepository;
import Mimo.Schedule.Repository.RoomRepository;
import Mimo.Schedule.Repository.SubjectRepository;
import Mimo.Schedule.Repository.TeacherRepository;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ScheduleMapper {

    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final RoomRepository roomRepository;

    public ScheduleMapper(GroupRepository groupRepository, SubjectRepository subjectRepository, TeacherRepository teacherRepository, RoomRepository roomRepository) {
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
    }

    public List<Schedule> mapToSchedules(List<ScheduleEntity> entities) {

        Set<Long> groupIds = entities.stream()
                .map(ScheduleEntity::getGroupId)
                .collect(Collectors.toSet());

        Set<Long> subjectIds = entities.stream()
                .map(ScheduleEntity::getSubjectId)
                .collect(Collectors.toSet());


        Set<Long> roomIds = entities.stream()
                .map(ScheduleEntity::getRoomId)
                .collect(Collectors.toSet());

        Set<Long> teacherIds = entities.stream()
                .map(ScheduleEntity::getTeacherId)
                .collect(Collectors.toSet());


        Map<Long, GroupEntity> groups = groupRepository.findAllById(groupIds)
                .stream()
                .collect(Collectors.toMap(
                        GroupEntity::getId,
                        Function.identity()
                ));

        Map<Long, SubjectEntity> subjects = subjectRepository.findAllById(subjectIds)
                .stream()
                .collect(Collectors.toMap(
                        SubjectEntity::getId,
                        Function.identity()
                ));


        Map<Long, RoomEntity> rooms = roomRepository.findAllById(roomIds)
                .stream()
                .collect(Collectors.toMap(
                        RoomEntity::getId,
                        Function.identity()
                ));

        Map<Long, TeacherEntity> teachers = teacherRepository.findAllById(teacherIds)
                .stream()
                .collect(Collectors.toMap(
                        TeacherEntity::getId,
                        Function.identity()
                ));


        return entities.stream()
                .map(entity -> new Schedule(
                        DayOfWeek.valueOf(entity.getDayOfWeek()),
                        groups.get(entity.getGroupId()).getName(),
                        subjects.get(entity.getSubjectId()).getName(),
                        entity.getType(),
                        rooms.get(entity.getRoomId()).getName(),
                        teachers.get(entity.getTeacherId()).getName(),
                        entity.getStartTime()
                ))
                .toList();
    }
}
