package Mimo.Schedule.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",  nullable = false,  unique = true)
    private String name;

    public RoomEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public RoomEntity() {
    }
}

