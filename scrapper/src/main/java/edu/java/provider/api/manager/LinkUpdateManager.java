package edu.java.provider.api.manager;

import edu.java.provider.api.LinkUpdateEvent;

public interface LinkUpdateManager<T> {
    LinkUpdateEvent collect(T t);

    boolean isChanged(LinkUpdateEvent oldEvent, LinkUpdateEvent newEvent);
}
