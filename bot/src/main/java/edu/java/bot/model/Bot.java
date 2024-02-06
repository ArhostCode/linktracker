package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;

public interface Bot extends AutoCloseable {

    void start();

    @Override
    void close();

    TelegramBot telegramBot();
}
