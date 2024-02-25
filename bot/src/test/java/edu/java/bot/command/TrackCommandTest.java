package edu.java.bot.command;

import edu.java.bot.Utils;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.dto.OptionalAnswer;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

public class TrackCommandTest {

    @DisplayName("Тестирование метода TrackCommand#handle с валидными данными и ответом сервера")
    @Test
    public void handleShouldTrackLink() {
        BotService mockBotService = Mockito.mock(BotService.class);
        Mockito.when(mockBotService.linkUrlToUser(Mockito.anyString(), Mockito.anyLong()))
            .thenReturn(OptionalAnswer.of(new LinkResponse(1L, URI.create("https://flame.ardyc.ru/generate"))));
        TrackCommand command = new TrackCommand(
            createMockTextResolver(),
            mockBotService
        );
        Assertions.assertThat(command.handle(Utils.createMockUpdate("/track https://flame.ardyc.ru/generate", 1L))
                .getParameters().get("text"))
            .isEqualTo("Link is tracked");
        Mockito.verify(mockBotService, Mockito.times(1)).linkUrlToUser("https://flame.ardyc.ru/generate", 1L);
    }

    @DisplayName("Тестирование метода TrackCommand#handle с валидными данными и ошибкой сервера")
    @Test
    public void handleShouldReturnErrorWhenServerError() {
        BotService mockBotService = Mockito.mock(BotService.class);
        Mockito.when(mockBotService.linkUrlToUser(Mockito.anyString(), Mockito.anyLong()))
            .thenReturn(OptionalAnswer.error(ApiErrorResponse.builder().description("Error").build()));
        TrackCommand command = new TrackCommand(
            createMockTextResolver(),
            mockBotService
        );
        Assertions.assertThat(command.handle(Utils.createMockUpdate("/track https://flame.ardyc.ru/generate", 1L))
                .getParameters().get("text"))
            .isEqualTo("Error");
        Mockito.verify(mockBotService, Mockito.times(1)).linkUrlToUser("https://flame.ardyc.ru/generate", 1L);
    }

    @DisplayName("Тестирование метода TrackCommand#handle с невалидными данными")
    @Test
    public void handleShouldReturnErrorWhenInvalidUrl() {
        BotService mockBotService = Mockito.mock(BotService.class);
        TrackCommand command = new TrackCommand(
            createMockTextResolver(),
            mockBotService
        );
        Assertions.assertThat(command.handle(Utils.createMockUpdate("/track asdsad", 1L))
                .getParameters().get("text"))
            .isEqualTo("Invalid url");
        Mockito.verifyNoInteractions(mockBotService);
    }

    @DisplayName("Тестирование метода TrackCommand#handle с невалидным исползованием команды")
    @Test
    public void handleShouldReturnErrorWhenInvalidCommandUsage() {
        BotService mockBotService = Mockito.mock(BotService.class);
        TrackCommand command = new TrackCommand(
            createMockTextResolver(),
            mockBotService
        );
        Assertions.assertThat(command.handle(Utils.createMockUpdate("/track", 1L))
                .getParameters().get("text"))
            .isEqualTo("Invalid command usage");
        Mockito.verifyNoInteractions(mockBotService);
    }

    private TextResolver createMockTextResolver() {
        TextResolver mockTextResolver = Mockito.mock(TextResolver.class, Answers.CALLS_REAL_METHODS);
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.track.success"), Mockito.anyMap()))
            .thenReturn("Link is tracked");
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.track.error"), Mockito.anyMap()))
            .thenReturn("Error");
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.track.invalid_command_usage"), Mockito.anyMap()))
            .thenReturn("Invalid command usage");
        Mockito.when(mockTextResolver.resolve(Mockito.eq("command.track.invalid_request"), Mockito.anyMap()))
            .thenReturn("Invalid url");
        return mockTextResolver;
    }

}
