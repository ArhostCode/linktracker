package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.executor.RequestExecutor;
import edu.java.bot.processor.UserMessagesProcessor;
import java.util.List;
import io.micrometer.core.instrument.MeterRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

public class BotMessagesListenerTest {
    @DisplayName("Тестирование метода BotMessagesListener#process")
    @Test
    public void processShouldAcceptUpdates() {
        BotMessagesListener listener = new BotMessagesListener(
            createMockExecutor(),
            createMockUserMessagesProcessor(),
            Mockito.mock(MeterRegistry.class)
        );
        Assertions.assertThat(listener.process(List.of(new Update()))).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
    }

    private RequestExecutor createMockExecutor() {
        return Mockito.mock(RequestExecutor.class);
    }

    private UserMessagesProcessor createMockUserMessagesProcessor() {
        return Mockito.mock(UserMessagesProcessor.class);
    }

}
