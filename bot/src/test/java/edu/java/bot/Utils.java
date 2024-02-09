package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.mockito.Mockito;

public class Utils {

    public static Update createMockUpdate(String text) {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);
        Mockito.when(mockMessage.text()).thenReturn(text);
        Mockito.when(mockUpdate.message()).thenReturn(mockMessage);
        return mockUpdate;
    }

    public static Update createMockUpdate(String text, Long chatId) {
        Update mockUpdate = Mockito.mock(Update.class);
        Message mockMessage = Mockito.mock(Message.class);
        Chat mockChat = Mockito.mock(Chat.class);
        Mockito.when(mockChat.id()).thenReturn(chatId);
        Mockito.when(mockMessage.text()).thenReturn(text);
        Mockito.when(mockUpdate.message()).thenReturn(mockMessage);
        Mockito.when(mockMessage.chat()).thenReturn(mockChat);
        return mockUpdate;
    }

}
