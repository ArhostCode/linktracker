package edu.java.service;

import edu.java.client.bot.request.LinkUpdate;

public interface LinkUpdateSender {

    void sendUpdate(LinkUpdate linkUpdate);
}
