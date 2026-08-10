package Mimo.schedule;
import jakarta.validation.constraints.Null;


public record Schedule(
        @Null
        Long id,
        Long group_id,
        Long subject_id,
        Long teacher_id,
        Long room_id,
        int period
){

}



