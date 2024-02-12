package edu.java.bot.command;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Utils;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.model.Link;
import edu.java.bot.model.response.ListLinksResponse;
import edu.java.bot.model.response.RemoveLinkFromTrackingResponse;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UntrackCommandTest {

    @DisplayName("Тестирование метода UntrackCommand#handle на создание сообщения с пустым списком ссылок")
    @Test
    public void handleShouldCreateMessageEmptyWhenListIsEmpty() {
        BotService botService = Mockito.mock(BotService.class);
        Mockito.when(botService.listLinks(Mockito.anyLong()))
            .thenReturn(new ListLinksResponse(Collections.emptyList()));
        UntrackCommand command = new UntrackCommand(
            createMockTextResolver(),
            botService
        );
        SendMessage message = command.handle(Utils.createMockUpdate("/untrack", 1L));
        Assertions.assertThat(message.getParameters().get("text"))
            .isEqualTo("Empty list");
    }

    @DisplayName("Тестирование метода UntrackCommand#handle на создание сообщения")
    @Test
    public void handleShouldCreateMessage() {
        BotService botService = Mockito.mock(BotService.class);
        Mockito.when(botService.listLinks(Mockito.anyLong()))
            .thenReturn(new ListLinksResponse(List.of(new Link("test", UUID.randomUUID()))));
        UntrackCommand command = new UntrackCommand(
            createMockTextResolver(),
            botService
        );
        SendMessage message = command.handle(Utils.createMockUpdate("/untrack", 1L));
        Assertions.assertThat(message.getParameters().get("text"))
            .isEqualTo("Main message");
        Assertions.assertThat(((InlineKeyboardMarkup) message.getParameters()
            .get("reply_markup"))
            .inlineKeyboard()[0]
        ).anyMatch(inlineKeyboardButton -> inlineKeyboardButton.text().equals("test"));
    }

    @DisplayName("Тестирование метода UntrackCommand#handle с callback")
    @Test
    public void handleShouldProcessCallback() {
        Link link = new Link("test", UUID.randomUUID());
        BotService botService = Mockito.mock(BotService.class);
        Mockito.when(botService.unlinkUrlFromUser(Mockito.any(), Mockito.anyLong()))
            .thenReturn(new RemoveLinkFromTrackingResponse(true, ""));
        UntrackCommand command = new UntrackCommand(
            createMockTextResolver(),
            botService
        );
        SendMessage message = command.handle(createMockUpdate(link));
        Assertions.assertThat(message.getParameters().get("text"))
            .isEqualTo("Success");
        Mockito.verify(botService, Mockito.times(1)).unlinkUrlFromUser(link.uuid(), 1L);
    }

    @DisplayName("Тестирование метода UntrackCommand#handle с callback с ошибкой")
    @Test
    public void handleShouldReturnErrorWhenServiceError() {
        Link link = new Link("test", UUID.randomUUID());
        BotService botService = Mockito.mock(BotService.class);
        Mockito.when(botService.unlinkUrlFromUser(Mockito.any(), Mockito.anyLong()))
            .thenReturn(new RemoveLinkFromTrackingResponse(false, ""));
        UntrackCommand command = new UntrackCommand(
            createMockTextResolver(),
            botService
        );
        SendMessage message = command.handle(createMockUpdate(link));
        Assertions.assertThat(message.getParameters().get("text"))
            .isEqualTo("Error");
        Mockito.verify(botService, Mockito.times(1)).unlinkUrlFromUser(link.uuid(), 1L);
    }

    private TextResolver createMockTextResolver() {
        TextResolver textResolver = Mockito.mock(TextResolver.class, Mockito.CALLS_REAL_METHODS);
        Mockito.when(textResolver.resolve(Mockito.eq("command.untrack.empty"), Mockito.any()))
            .thenReturn("Empty list");
        Mockito.when(textResolver.resolve(Mockito.eq("command.untrack.main"), Mockito.any()))
            .thenReturn("Main message");
        Mockito.when(textResolver.resolve(Mockito.eq("command.untrack.success"), Mockito.any()))
            .thenReturn("Success");
        Mockito.when(textResolver.resolve(Mockito.eq("command.untrack.error"), Mockito.any()))
            .thenReturn("Error");
        return textResolver;
    }

    private Update createMockUpdate(Link untrackLink) {
        Update update = Mockito.mock(Update.class);
        CallbackQuery callbackQuery = Mockito.mock(CallbackQuery.class);
        User user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(1L);
        Mockito.when(callbackQuery.data()).thenReturn(String.format("untrack$%s", untrackLink.uuid()));
        Mockito.when(callbackQuery.from()).thenReturn(user);
        Mockito.when(update.callbackQuery()).thenReturn(callbackQuery);
        return update;
    }

}
