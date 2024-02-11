package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Utils;
import edu.java.bot.commands.AbstractCommand;
import edu.java.bot.util.TextResolver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Answers;
import org.mockito.Mockito;

public class AbstractCommandTest {

    @DisplayName("Тестирование метода AbstractCommand#supports")
    @ParameterizedTest
    @CsvSource(
        value = {
            "/mock,true",
            "/dfdf,false",
        }
    )
    public void supportsShouldReturnTrueWhenUpdateSupports(String text, boolean expected) {
        AbstractCommand command = new AbstractCommand(createMockTextResolver()) {
            @Override
            public String command() {
                return "/mock";
            }

            @Override
            public String description() {
                return null;
            }

            @Override
            public SendMessage handle(Update update) {
                return null;
            }
        };

        Assertions.assertThat(command.supports(Utils.createMockUpdate(text))).isEqualTo(expected);
    }

    @DisplayName("Тестирование метода AbstractCommand#toApiCommand")
    @Test
    public void toApiCommandShouldReturnCommand() {
        AbstractCommand command = new AbstractCommand(createMockTextResolver()) {
            @Override
            public String command() {
                return "/mock";
            }

            @Override
            public String description() {
                return "mock.description";
            }

            @Override
            public SendMessage handle(Update update) {
                return null;
            }
        };
        Assertions.assertThat(command.toApiCommand()).isEqualTo(new BotCommand("/mock", "Mock message"));
    }

    private TextResolver createMockTextResolver() {
        TextResolver mockTextResolver = Mockito.mock(TextResolver.class, Answers.CALLS_REAL_METHODS);
        Mockito.when(mockTextResolver.resolve(Mockito.eq("mock.description"), Mockito.anyMap()))
            .thenReturn("Mock message");
        return mockTextResolver;
    }

}
