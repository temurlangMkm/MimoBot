package Mimo.schedule;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;
    private final ScheduleMapper mapper;

    public ScheduleService(ScheduleRepository repository, ScheduleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


   //___________________________________________//

    public List<Schedule> getScheduleByDayOfWeekAndGroupId(
            Long group_id,
            int dayOfWeek               // 1=Monday, 2=Tuesday, ... 7 = Sunday
    ){
        var entityList = repository.getScheduleByDayOfWeekAndGroupId(group_id, dayOfWeek);

        if(entityList.isEmpty()){
            throw new EntityNotFoundException("Not found Schedule for group by id "+group_id+" and day of week "+dayOfWeek);
        }

        return entityList.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Schedule> getScheduleForWeekByGroupId(
            Long group_id
    ){
        var entityList = repository.getScheduleForWeekByGroupId(group_id);

        if(entityList.isEmpty()){
            throw new EntityNotFoundException("Not found group by id "+group_id);
        }

        return entityList.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Schedule> UpdateSchedule(
            Long group_id
    ){
        var entityListForUpdate = repository.getScheduleForWeekByGroupId(group_id);

        if(entityListForUpdate.isEmpty()){
            throw new EntityNotFoundException("Not found group by id "+group_id);
        }

        return null;
    }

    public List<Schedule> deleteScheduleByGroup(
            Long group_id
    ){
        if(repository.existsByGroup_Id(group_id)){
            throw new EntityNotFoundException("Not found group by id "+group_id);
        }
        repository.deleteByGroup_Id(group_id);
        return null;
    }


}
