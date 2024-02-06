package edu.java.bot.executor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

public interface RequestExecutor {
    void updateTelegramBot(TelegramBot telegramBot);

    <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request);
}
