package pro.sky.telegrambot.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification_task", schema = "public", catalog = "telegram")
public class NotificationTask {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "notification")
    private String notification;

    @Column(name = "send_date")
    private LocalDateTime sendDate;
}
