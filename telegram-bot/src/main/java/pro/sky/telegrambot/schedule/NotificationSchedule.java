package pro.sky.telegrambot.schedule;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import pro.sky.telegrambot.entities.NotificationTask;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repositories.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class NotificationSchedule {
    private Logger logger = LoggerFactory.getLogger(NotificationSchedule.class);

    private final TelegramBotUpdatesListener telegramBotUpdatesListener;

    private final NotificationTaskRepository notificationTaskRepository;

    @Scheduled(cron = "${data.schedule.notification-schedule.cron}")
    public void execute() {
        logger.info("Processing scheduling");
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> list = notificationTaskRepository.findNotificationTasksBySendDateBetween(now, now.plusSeconds(59));
        list.forEach(i -> telegramBotUpdatesListener.sendMessage(i.getChatId(), i.getNotification()));
    }
}
