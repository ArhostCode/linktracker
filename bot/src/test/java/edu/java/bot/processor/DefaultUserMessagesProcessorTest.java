package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Utils;
import edu.java.bot.commands.Command;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

public class DefaultUserMessagesProcessorTest {

    @DisplayName("Тестирование метода DefaultUserMessagesProcessor#registerCommand")
    @Test
    public void registerCommandShouldRegisterCommand() {
        UserMessagesProcessor processor = new DefaultUserMessagesProcessor(
            createMockTextResolver(),
            Mockito.mock(BotService.class)
        );
        processor.registerCommand(createMockCommand());
        Assertions.assertThat(processor.commands()).anyMatch(
            command -> command.command().equals("/mock")
        );
    }

    @DisplayName("Тестирование метода DefaultUserMessagesProcessor#process с корректными командой и сообщением")
    @Test
    public void processShouldExecuteCommand() {
        UserMessagesProcessor processor = new DefaultUserMessagesProcessor(
            createMockTextResolver(),
            Mockito.mock(BotService.class)
        );
        processor.registerCommand(createMockCommand());

        Assertions.assertThat(processor.process(Utils.createMockUpdate("/mock")).getParameters().get("text"))
            .isEqualTo("Mock message");
    }

    @DisplayName("Тестирование метода DefaultUserMessagesProcessor#process с корректной командой и null сообщением")
    @Test
    public void processShouldReturnNullWhenMessageIsNull() {
        UserMessagesProcessor processor = new DefaultUserMessagesProcessor(
            createMockTextResolver(),
            Mockito.mock(BotService.class)
        );
        processor.registerCommand(createMockCommand());

        Assertions.assertThat(processor.process(new Update())).isNull();
    }

    @DisplayName(
        "Тестирование метода DefaultUserMessagesProcessor#process с корректной командой и некорректным сообщением")
    @Test
    public void processShouldReturnNotFoundWhenCommandNotFound() {
        UserMessagesProcessor processor = new DefaultUserMessagesProcessor(
            createMockTextResolver(),
            Mockito.mock(BotService.class)
        );
        processor.registerCommand(createMockCommand());

        Update mockUpdate = Utils.createMockUpdate("/mock-not-found", 1L);
        Assertions.assertThat(processor.process(mockUpdate).getParameters().get("text"))
            .isEqualTo("Mock");
        Assertions.assertThat(processor.process(mockUpdate).getParameters().get("chat_id"))
            .isEqualTo(1L);
    }

    private TextResolver createMockTextResolver() {
        TextResolver mockTextResolver = Mockito.mock(TextResolver.class);
        Mockito.when(mockTextResolver.resolve(Mockito.anyString(), Mockito.anyMap())).thenReturn("Mock");
        return mockTextResolver;
    }

    private Command createMockCommand() {
        Command command = Mockito.mock(Command.class, Answers.CALLS_REAL_METHODS);
        Mockito.when(command.command()).thenReturn("/mock");
        Mockito.when(command.handle(Mockito.any())).thenReturn(new SendMessage(1, "Mock message"));
        return command;
    }
}
