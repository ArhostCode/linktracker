package edu.java.persitence.jooq.repository;

import edu.java.domain.dto.TgChat;
import edu.java.persitence.common.repository.TgChatRepository;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import static edu.java.domain.jooq.Tables.TG_CHAT;

@Component
public class JooqChatRepository implements TgChatRepository {

    private final DSLContext dslContext;

    public JooqChatRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<TgChat> findAll() {
        return dslContext.select(TG_CHAT.fields())
            .from(TG_CHAT)
            .fetchInto(TgChat.class);
    }

    @Override
    public void add(long chatId) {
        dslContext.insertInto(TG_CHAT, TG_CHAT.ID)
            .values(chatId)
            .execute();
    }

    @Override
    public void remove(long chatId) {
        dslContext.delete(TG_CHAT)
            .where(TG_CHAT.ID.eq(chatId))
            .execute();
    }

    @Override
    public boolean isExists(long chatId) {
        return dslContext.fetchCount(TG_CHAT, TG_CHAT.ID.eq(chatId)) > 0;
    }
}
