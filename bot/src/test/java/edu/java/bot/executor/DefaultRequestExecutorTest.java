package edu.java.bot.executor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DefaultRequestExecutorTest {

    @DisplayName(
        "Тестирование метода DefaultRequestExecutor#execute должен выкинуть исключение если telegramBot не задан"
    )
    @Test
    public void execute_shouldThrowIllegalStateException_whenTelegramBotNotSet() {
        RequestExecutor executor = new DefaultRequestExecutor(null);
        Assertions.assertThatThrownBy(() -> executor.execute(new SendMessage(1, "Test message")))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName(
        "Тестирование метода DefaultRequestExecutor#execute должен выполнить запрос если telegramBot задан"
    )
    @Test
    public void execute_shouldExecute_whenTelegramBotSet() {
        TelegramBot mockTelegramBot = Mockito.mock(TelegramBot.class);
        RequestExecutor executor = new DefaultRequestExecutor(mockTelegramBot);

        executor.execute(new SendMessage(1, "Test message"));

        Mockito.verify(mockTelegramBot, Mockito.times(1)).execute(Mockito.any(SendMessage.class));
    }
}
