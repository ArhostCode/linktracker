package edu.java.bot.service;

import edu.java.bot.dto.Link;
import java.util.List;

public interface LinkNotificationService {

    void notifyLinkUpdate(Link link, List<Long> chatIds);

}
