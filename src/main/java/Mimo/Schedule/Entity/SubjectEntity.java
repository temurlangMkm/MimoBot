package Mimo.Schedule.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "subjects")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",  nullable = false,  unique = true)
    private String name;

    public SubjectEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public SubjectEntity() {
    }
}

