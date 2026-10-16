package Mimo.Telegram.Entity;

import Mimo.Telegram.Service.Language;
import Mimo.Telegram.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tg_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tg_id", nullable = false, unique = true)
    private Long tgId;


    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "notifications")
    private Boolean notifications;

    @Column(name = "only_command")
    private Boolean onlyCommand;

    @Column(name = "language", nullable = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    public UserEntity() {
    }

    public UserEntity(
            Long id,
            Long tgID,
            Long groupId,
            Boolean notifications,
            Boolean onlyCommand,
            Language language
    ) {
        this.id = id;
        this.tgId = tgID;
        this.groupId = groupId;
        this.notifications = notifications;
        this.onlyCommand = onlyCommand;
        this.language = language;
    }

}
