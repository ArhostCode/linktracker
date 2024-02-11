package edu.java.bot.command;

import edu.java.bot.Utils;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.response.ListLinksResponse;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

public class ListCommandTest {

    @DisplayName("Тестирование метода ListCommand#handle с пустым списком")
    @Test
    public void handleShouldReturnEmptyListMessageWhenListIsEmpty() {
        BotService mockBotService = Mockito.mock(BotService.class);
        Mockito.when(mockBotService.listLinks(Mockito.anyLong()))
            .thenReturn(new ListLinksResponse(List.of()));
        ListCommand command = new ListCommand(
            createMockTextResolver(),
            mockBotService
        );

        Assertions.assertThat(command.handle(Utils.createMockUpdate("/list", 1L)).getParameters().get("text"))
            .isEqualTo("List is empty");
        Mockito.verify(mockBotService, Mockito.times(1)).listLinks(1L);
    }

    @DisplayName("Тестирование метода ListCommand#handle с непустым списком")
    @Test
    public void handleShouldReturnListMessageWhenListIsNotEmpty() {
        BotService mockBotService = Mockito.mock(BotService.class);
        Mockito.when(mockBotService.listLinks(Mockito.anyLong()))
            .thenReturn(new ListLinksResponse(List.of(
                new Link("link1", UUID.randomUUID()),
                new Link("link2", UUID.randomUUID())
            )));
        ListCommand command = new ListCommand(
            createMockTextResolver(),
            mockBotService
        );

        Assertions.assertThat(command.handle(Utils.createMockUpdate("/list", 1L)).getParameters().get("text"))
            .isEqualTo("List:\n1. link1\n2. link2\n");
        Mockito.verify(mockBotService, Mockito.times(1)).listLinks(1L);
    }

    private TextResolver createMockTextResolver() {
        TextResolver mockTextResolver = Mockito.mock(TextResolver.class, Answers.CALLS_REAL_METHODS);
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.list.empty"), Mockito.anyMap()))
            .thenReturn("List is empty");
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.list.main"), Mockito.anyMap()))
            .thenReturn("List:\n");
        return mockTextResolver;
    }

}
