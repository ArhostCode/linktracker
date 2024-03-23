package edu.java.persitence.common.repository;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.TgChat;
import java.util.List;

public interface TgChatLinkRepository {

    void add(long chatId, long linkId);

    void remove(long chatId, long linkId);

    List<Link> findAllByChatId(long chatId);

    List<TgChat> findAllByLinkId(long linkId);

    void removeAllByChatId(Long chatId);

    boolean isExists(long chatId, long linkId);
}
