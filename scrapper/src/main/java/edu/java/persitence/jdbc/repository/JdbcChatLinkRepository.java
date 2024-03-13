package edu.java.persitence.jdbc.repository;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.dto.TgChat;
import edu.java.persitence.common.repository.TgChatLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements TgChatLinkRepository {

    private final JdbcClient client;

    @Override
    public void add(long chatId, long linkId) {
        client.sql("INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)")
            .params(List.of(chatId, linkId))
            .update();
    }

    @Override
    public void remove(long chatId, long linkId) {
        client.sql("DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?")
            .params(List.of(chatId, linkId))
            .update();
    }

    @Override
    public List<Link> findAllByChatId(long chatId) {
        return client.sql("""
                SELECT
                  link.*
                FROM
                  chat_link
                  INNER JOIN link ON link.id = chat_link.link_id
                WHERE
                  chat_link.chat_id = ?""")
            .params(chatId)
            .query(Link.class)
            .list();
    }

    @Override
    public List<TgChat> findAllByLinkId(long linkId) {
        return client.sql("""
                SELECT
                  tg_chat.*
                FROM
                  chat_link
                  INNER JOIN tg_chat ON tg_chat.id = chat_link.chat_id
                WHERE
                  chat_link.link_id = ?""")
            .params(linkId)
            .query(TgChat.class)
            .list();
    }

    @Override
    public void removeAllByChatId(Long chatId) {
        client.sql("DELETE FROM chat_link WHERE chat_id = ?")
            .params(chatId)
            .update();
    }
}



