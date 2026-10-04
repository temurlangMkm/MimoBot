package Mimo.Schedule.Repository;

import Mimo.Schedule.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findAllByNameIn(Collection<String> rooms);
}
