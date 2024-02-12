package edu.java.bot.model;

public interface Bot extends AutoCloseable {

    void start();

    @Override
    void close();
}
