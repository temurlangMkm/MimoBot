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
public class ChatEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    public ChatEntity() {
    }

    public ChatEntity(
            Long id,
            Long tgId,
            Long groupID,
            Boolean notifications,
            Boolean onlyCommand,
            Language language
    ) {
        this.id = id;
        this.tgId = tgId;
        this.groupId = groupID;
        this.notifications = notifications;
        this.onlyCommand = onlyCommand;
        this.language = language;
    }

}

