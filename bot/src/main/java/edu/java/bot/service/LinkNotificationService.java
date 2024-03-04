package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;

public interface LinkNotificationService {

    void notifyLinkUpdate(LinkUpdate link);

}
