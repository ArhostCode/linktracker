package edu.java.persitence.common.repository;

import edu.java.domain.dto.TgChat;
import java.util.List;

public interface TgChatRepository {

    List<TgChat> findAll();

    void add(long chatId);

    void remove(long chatId);

    boolean isExists(long chatId);

}
