package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.processor.UserMessagesProcessor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotMessagesListener implements UpdatesListener {

    private final RequestExecutor requestExecutor;
    private final UserMessagesProcessor userMessagesProcessor;
    private final MeterRegistry meterRegistry;
    private Counter userMessagesCounter;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            SendMessage sendMessage = userMessagesProcessor.process(update);
            if (sendMessage != null) {
                userMessagesCounter.increment();
                requestExecutor.execute(sendMessage.parseMode(ParseMode.Markdown));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    public void initMetrics() {
        userMessagesCounter = Counter.builder("user_messages")
            .description("Count of processed user messages")
            .register(meterRegistry);
    }
}
