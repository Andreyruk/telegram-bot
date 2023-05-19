package pro.sky.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entities.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    @Query("select n from NotificationTask n where n.sendDate between ?1 and ?2")
    List<NotificationTask> findNotificationTasksBySendDateBetween(LocalDateTime d1, LocalDateTime d2);
}
