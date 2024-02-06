package edu.java.bot.service;

public interface BotService {

    void registerUser(String name, String id);

    void linkUrlToUser(String url, String userId);

    void unlinkUrlFromUser(String url, String userId);

}
