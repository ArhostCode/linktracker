package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.listener.BotMessagesListener;
import edu.java.bot.model.Bot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkTrackerBot implements Bot {

    private TelegramBot telegramBot;
    private final ApplicationConfig config;
    private final BotMessagesListener botMessagesListener;
    private final RequestExecutor requestExecutor;

    @Override
    @PostConstruct
    public void start() {
        telegramBot = new TelegramBot(config.telegramToken());
        requestExecutor.updateTelegramBot(telegramBot);
        telegramBot.setUpdatesListener(botMessagesListener);
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
