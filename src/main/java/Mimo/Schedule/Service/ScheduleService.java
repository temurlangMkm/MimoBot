package Mimo.Schedule.Service;

import Mimo.Schedule.DTO.Schedule;
import Mimo.Schedule.Entity.RoomEntity;
import Mimo.Schedule.Entity.ScheduleEntity;
import Mimo.Schedule.Entity.SubjectEntity;
import Mimo.Schedule.Entity.TeacherEntity;
import Mimo.Schedule.Repository.RoomRepository;
import Mimo.Schedule.Repository.ScheduleRepository;
import Mimo.Schedule.Repository.SubjectRepository;
import Mimo.Schedule.Repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ObjectMapper mapper;

    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final RoomRepository roomRepository;


    public ScheduleService(ObjectMapper scheduleMapper, ScheduleRepository scheduleRepository, GroupService groupService, SubjectRepository subjectRepository, TeacherRepository teacherRepository, RoomRepository roomRepository) {
        this.mapper = scheduleMapper;
        this.scheduleRepository = scheduleRepository;
        this.groupService = groupService;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public String setSchedule(String message, String groupName) {

        List<Schedule> schedules;

        try {
            schedules = parseSchedules(message);
        } catch (JacksonException e) {
            return e.getLocalizedMessage();
        }

        prepareSchedules(schedules, groupName);

        Map<String, Long> subjectsMap = loadSubjects(schedules);
        Map<String, Long> roomsMap = loadRooms(schedules);
        Map<String, Long> teachersMap = loadTeachers(schedules);

        Long groupId = groupService.getIdByName(groupName);

        List<ScheduleEntity> scheduleEntities = buildScheduleEntities(
                schedules,
                groupId,
                subjectsMap,
                roomsMap,
                teachersMap
        );

        scheduleRepository.saveAll(scheduleEntities);

        return schedules.toString();
    }

    private List<Schedule> parseSchedules(String message) throws JacksonException {

        return mapper.readValue(
                message,
                new TypeReference<List<Schedule>>() {}
        );
    }

    private void prepareSchedules(List<Schedule> schedules, String groupName) {

        for (Schedule schedule : schedules) {
            schedule.setGroup(groupName);
        }
    }

    private Map<String, Long> loadSubjects(List<Schedule> schedules) {

        Set<String> names = schedules.stream()
                .map(Schedule::getSubject)
                .collect(Collectors.toSet());

        List<SubjectEntity> entities = subjectRepository.findAllByNameIn(names);

        Map<String, Long> result = entities.stream()
                .collect(Collectors.toMap(
                        SubjectEntity::getName,
                        SubjectEntity::getId
                ));

        for (Schedule schedule : schedules) {

            String name = schedule.getSubject();

            if (!result.containsKey(name)) {

                SubjectEntity entity = new SubjectEntity();
                entity.setName(name);

                Long id = subjectRepository.save(entity).getId();
                subjectRepository.flush();

                result.put(name, id);
            }
        }

        return result;
    }

    private Map<String, Long> loadRooms(List<Schedule> schedules) {

        Set<String> names = schedules.stream()
                .map(Schedule::getRoom)
                .collect(Collectors.toSet());

        List<RoomEntity> entities = roomRepository.findAllByNameIn(names);

        Map<String, Long> result = entities.stream()
                .collect(Collectors.toMap(
                        RoomEntity::getName,
                        RoomEntity::getId
                ));

        for (Schedule schedule : schedules) {

            String name = schedule.getRoom();

            if (!result.containsKey(name)) {

                RoomEntity entity = new RoomEntity();
                entity.setName(name);

                Long id = roomRepository.save(entity).getId();
                roomRepository.flush();

                result.put(name, id);
            }
        }

        return result;
    }

    private Map<String, Long> loadTeachers(List<Schedule> schedules) {

        Set<String> names = schedules.stream()
                .map(Schedule::getTeacher)
                .collect(Collectors.toSet());

        List<TeacherEntity> entities =
                teacherRepository.findAllByNameIn(names);

        Map<String, Long> result = entities.stream()
                .collect(Collectors.toMap(
                        TeacherEntity::getName,
                        TeacherEntity::getId
                ));

        for (Schedule schedule : schedules) {

            String name = schedule.getTeacher();

            if (!result.containsKey(name)) {

                TeacherEntity entity = new TeacherEntity();
                entity.setName(name);

                Long id = teacherRepository.save(entity).getId();
                teacherRepository.flush();;

                result.put(name, id);
            }
        }

        return result;
    }

    private List<ScheduleEntity> buildScheduleEntities(
            List<Schedule> schedules,
            Long groupId,
            Map<String, Long> subjectsMap,
            Map<String, Long> roomsMap,
            Map<String, Long> teachersMap
    ) {

        List<ScheduleEntity> result = new ArrayList<>();

        for (Schedule schedule : schedules) {

            ScheduleEntity entity = new ScheduleEntity();

            entity.setGroupId(groupId);
            entity.setDayOfWeek(String.valueOf(schedule.getDayOfWeek()));
            entity.setSubjectId(subjectsMap.get(schedule.getSubject()));
            entity.setRoomId(roomsMap.get(schedule.getRoom()));
            entity.setTeacherId(teachersMap.get(schedule.getTeacher()));
            entity.setType(schedule.getType());
            entity.setStartTime(schedule.getStartTime());

            result.add(entity);
        }

        return result;
    }


}
