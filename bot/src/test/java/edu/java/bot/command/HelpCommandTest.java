package edu.java.bot.command;

import edu.java.bot.Utils;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.util.TextResolver;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

public class HelpCommandTest {

    @DisplayName("Тестирование метода HelpCommand#handle")
    @Test
    public void handleShouldReturnHelpMessage() {
        HelpCommand command = new HelpCommand(createMockTextResolver(), List.of(createMockCommand()));
        Assertions.assertThat(command.handle(Utils.createMockUpdate("/help", 1L)).getParameters().get("text"))
            .isEqualTo("Main message/mock - mock description");
    }

    private TextResolver createMockTextResolver() {
        TextResolver mockTextResolver = Mockito.mock(TextResolver.class, Answers.CALLS_REAL_METHODS);
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.help.main"), Mockito.anyMap()))
            .thenReturn("Main message");
        Mockito.when(mockTextResolver.resolve(Mockito.eq("mock.description"), Mockito.anyMap()))
            .thenReturn("mock description");
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.help.format"), Mockito.anyMap()))
            .thenReturn("%command% - %description%");
        return mockTextResolver;
    }

    private Command createMockCommand() {
        Command mockCommand = Mockito.mock(Command.class);
        Mockito.when(mockCommand.command()).thenReturn("/mock");
        Mockito.when(mockCommand.description()).thenReturn("mock.description");
        return mockCommand;
    }

}
