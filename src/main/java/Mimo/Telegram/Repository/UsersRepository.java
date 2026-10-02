package Mimo.Telegram.Repository;

import Mimo.Telegram.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {


    boolean existsByTgId(Long tgId);

    UserEntity findByTgId(Long chatId);

    void deleteByTgId(Long chatId);
}
