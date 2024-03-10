package edu.java.persitence.common.repository;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.dto.TgChat;
import java.util.List;

public interface TgChatLinkRepository {

    void add(long chatId, long linkId);

    void remove(long chatId, long linkId);

    List<Link> findAllByChatId(long chatId);

    List<TgChat> findAllByLinkId(long linkId);

    void removeAllByChatId(Long chatId);
}
