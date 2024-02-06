package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.processor.UserMessagesProcessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotMessagesListener implements UpdatesListener {

    private final RequestExecutor requestExecutor;
    private final UserMessagesProcessor userMessagesProcessor;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message().text() != null) {
                SendMessage sendMessage = userMessagesProcessor.process(update);
                if (sendMessage != null) {
                    requestExecutor.execute(sendMessage.parseMode(ParseMode.Markdown));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
