package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.listener.BotMessagesListener;
import edu.java.bot.processor.UserMessagesProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LinkTrackerBotTest {

    @DisplayName("Тестирование метода LinkTrackerBot#start")
    @Test
    public void start_shouldStart() {

        TelegramBot bot = Mockito.mock(TelegramBot.class);
        LinkTrackerBot linkTrackerBot = new LinkTrackerBot(
            bot,
            Mockito.mock(BotMessagesListener.class),
            Mockito.mock(UserMessagesProcessor.class),
            Mockito.mock(RequestExecutor.class)
        );
        linkTrackerBot.start();
        Mockito.verify(bot, Mockito.times(1))
            .setUpdatesListener(Mockito.any(BotMessagesListener.class));
    }
}
