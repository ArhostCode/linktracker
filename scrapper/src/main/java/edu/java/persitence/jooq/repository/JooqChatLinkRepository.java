package edu.java.persitence.jooq.repository;

import edu.java.persitence.common.dto.Link;
import edu.java.persitence.common.dto.TgChat;
import edu.java.persitence.common.repository.TgChatLinkRepository;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;
import static edu.java.domain.jooq.Tables.TG_CHAT;

@Component
public class JooqChatLinkRepository implements TgChatLinkRepository {

    private final DSLContext dslContext;

    public JooqChatLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void add(long chatId, long linkId) {
        dslContext.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
            .values(chatId, linkId)
            .execute();
    }

    @Override
    public void remove(long chatId, long linkId) {
        dslContext.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId)))
            .execute();
    }

    @Override
    public List<Link> findAllByChatId(long chatId) {
        return dslContext.select(LINK.fields()).from(CHAT_LINK)
            .join(LINK)
            .on(CHAT_LINK.LINK_ID.eq(LINK.ID))
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    @Override
    public List<TgChat> findAllByLinkId(long linkId) {
        return dslContext.select(CHAT_LINK.fields()).from(CHAT_LINK)
            .join(TG_CHAT)
            .on(CHAT_LINK.CHAT_ID.eq(TG_CHAT.ID))
            .where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(TgChat.class);
    }

    @Override
    public void removeAllByChatId(Long chatId) {
        dslContext.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .execute();
    }

    @Override
    public boolean isExists(long chatId, long linkId) {
        return dslContext.fetchCount(CHAT_LINK, CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId))) > 0;
    }
}
