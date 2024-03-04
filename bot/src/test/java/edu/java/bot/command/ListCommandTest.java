package edu.java.bot.command;

import edu.java.bot.Utils;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.dto.OptionalAnswer;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.net.URI;
import java.util.List;
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
            .thenReturn(OptionalAnswer.of(new ListLinksResponse(List.of(), 0)));
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
            .thenReturn(OptionalAnswer.of(new ListLinksResponse(List.of(
                new LinkResponse(1L, URI.create("http://localhost.ru")),
                new LinkResponse(2L, URI.create("http://localhost2.ru"))
            ), 2)));
        ListCommand command = new ListCommand(
            createMockTextResolver(),
            mockBotService
        );

        Assertions.assertThat(command.handle(Utils.createMockUpdate("/list", 1L)).getParameters().get("text"))
            .isEqualTo("List:\n1. http://localhost.ru\n2. http://localhost2.ru\n");
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
