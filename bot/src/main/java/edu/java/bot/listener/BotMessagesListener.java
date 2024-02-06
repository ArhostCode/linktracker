package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.processor.UserMessagesProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotMessagesListener implements UpdatesListener {

    private RequestExecutor requestExecutor;
    private UserMessagesProcessor userMessagesProcessor;

    @Autowired
    public void setUserMessagesProcessor(UserMessagesProcessor userMessagesProcessor) {
        this.userMessagesProcessor = userMessagesProcessor;
    }

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null) {
                SendMessage sendMessage = userMessagesProcessor.process(update);
                if (sendMessage != null) {
                    requestExecutor.execute(sendMessage);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
