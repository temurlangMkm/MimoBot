package Mimo.Schedule.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "groups")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",  nullable = false,  unique = true)
    private String name;

    public GroupEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public GroupEntity() {
    }
}
