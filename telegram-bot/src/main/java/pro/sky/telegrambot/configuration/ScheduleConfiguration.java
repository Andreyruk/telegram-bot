package pro.sky.telegrambot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.repositories.NotificationTaskRepository;
import pro.sky.telegrambot.schedule.NotificationSchedule;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {
    @Bean
    public NotificationSchedule notificationSchedule(@Autowired TelegramBotUpdatesListener telegramBotUpdatesListener, @Autowired NotificationTaskRepository notificationTaskRepository) {
        return new NotificationSchedule(telegramBotUpdatesListener, notificationTaskRepository);
    }
}
