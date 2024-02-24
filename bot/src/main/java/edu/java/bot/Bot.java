package edu.java.bot;

public interface Bot extends AutoCloseable {

    void start();

    @Override
    void close();
}
