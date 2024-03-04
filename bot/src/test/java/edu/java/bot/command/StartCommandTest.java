package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Utils;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

public class StartCommandTest {

    @DisplayName("Тестирование метода StartCommand#handle")
    @Test
    public void handleShouldRegisterUser() {
        BotService mockBotService = Mockito.mock(BotService.class);
        StartCommand command = new StartCommand(
            createMockTextResolver(),
            mockBotService
        );

        Update update = Utils.createMockUpdate("/start", 1L);
        Mockito.when(update.message().chat().firstName()).thenReturn("1");
        SendMessage message = command.handle(update);
        Assertions.assertThat(message.getParameters().get("text"))
            .isEqualTo("Hello");
        Mockito.verify(mockBotService, Mockito.times(1)).registerUser(1L);
    }

    private TextResolver createMockTextResolver() {
        TextResolver mockTextResolver = Mockito.mock(TextResolver.class, Answers.CALLS_REAL_METHODS);
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.start.hello_message"), Mockito.anyMap()))
            .thenReturn("Hello");
        return mockTextResolver;
    }
}
