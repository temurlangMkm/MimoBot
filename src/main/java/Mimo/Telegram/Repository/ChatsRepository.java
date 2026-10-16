package Mimo.Telegram.Repository;

import Mimo.Telegram.Entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatsRepository extends JpaRepository<ChatEntity, Long> {


    boolean existsByTgId(Long tgId);

    ChatEntity findByTgId(Long chatId);

    void deleteByTgId(Long chatId);
}
