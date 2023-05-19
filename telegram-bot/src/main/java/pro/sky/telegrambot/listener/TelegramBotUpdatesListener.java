package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entities.NotificationTask;
import pro.sky.telegrambot.repositories.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private final NotificationTaskRepository notificationTaskRepository;

    private boolean start;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {
            long chatId = update.message().chat().id();
            String text = update.message().text();
            if (text.equals("/start")) {
                this.start = true;
                sendMessage(chatId, "Hello!");
            } else if (text.equals("/stop")) {
                this.start = false;
                sendMessage(chatId, "By!");
            } else {
                if (this.start) {

                    Matcher matcher = Pattern.compile("[0-9\\.\\:\\s]{16}").matcher(update.message().text());
                    matcher.find();
                    String notificationDate = matcher.group().trim();
                    matcher = Pattern.compile("(\\s)([\\W+]+)").matcher(update.message().text());
                    matcher.find();
                    String notificationMessage = matcher.group().trim();
                    saveMessage(chatId, notificationDate, notificationMessage);
                } else {
                    sendMessage(chatId, "Для старта наберите: /start");
                }
            }
            logger.info("Processing update: {}", update);
            // Process your updates here
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Transactional
    public void saveMessage(Long chatId, String notificationDate, String notificationMessage) {
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(chatId);
        notificationTask.setNotification(notificationMessage);
        if (!notificationDate.isEmpty())
            notificationTask.setSendDate(LocalDateTime.parse(notificationDate, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        notificationTaskRepository.save(notificationTask);
    }

    public void sendMessage(Long chatId, String message) {
        telegramBot.execute(new SendMessage(chatId, message));
    }
}
