package edu.java.bot.executor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class DefaultRequestExecutor implements RequestExecutor {

    private TelegramBot telegramBot;

    public void updateTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        if (telegramBot == null) {
            throw new IllegalStateException("Telegram bot is not working");
        }
        telegramBot.execute(request);
    }
}
