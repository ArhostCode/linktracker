package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationProperties;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.listener.BotMessagesListener;
import edu.java.bot.model.Bot;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class LinkTrackerBot implements Bot {

    private TelegramBot telegramBot;
    private ApplicationProperties config;
    private BotMessagesListener botMessagesListener;
    private RequestExecutor requestExecutor;

    public LinkTrackerBot(
        ApplicationProperties config,
        BotMessagesListener botMessagesListener,
        RequestExecutor requestExecutor
    ) {
        this.config = config;
        this.botMessagesListener = botMessagesListener;
        this.requestExecutor = requestExecutor;
    }

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

    @Override
    public TelegramBot telegramBot() {
        return telegramBot;
    }

}
