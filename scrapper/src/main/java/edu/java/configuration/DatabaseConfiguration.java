package edu.java.configuration;

import edu.java.persitence.common.service.DefaultChatService;
import edu.java.persitence.common.service.DefaultLinkService;
import edu.java.persitence.jdbc.repository.JdbcChatLinkRepository;
import edu.java.persitence.jdbc.repository.JdbcChatRepository;
import edu.java.persitence.jdbc.repository.JdbcLinkRepository;
import edu.java.persitence.jooq.repository.JooqChatLinkRepository;
import edu.java.persitence.jooq.repository.JooqChatRepository;
import edu.java.persitence.jooq.repository.JooqLinkRepository;
import edu.java.provider.InformationProviders;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "database.accessor", havingValue = "jdbc")
    public ChatService jdbcChatService(
        JdbcChatRepository tgChatRepository,
        JdbcChatLinkRepository tgChatLinkRepository,
        JdbcLinkRepository linkRepository
    ) {
        return new DefaultChatService(tgChatRepository, tgChatLinkRepository, linkRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "database.accessor", havingValue = "jdbc")
    public LinkService jdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository tgChatLinkRepository,
        InformationProviders informationProviders
    ) {
        return new DefaultLinkService(linkRepository, tgChatLinkRepository, informationProviders);
    }

    @Bean
    @ConditionalOnProperty(name = "database.accessor", havingValue = "jooq")
    public ChatService jooqChatService(
        JooqChatRepository tgChatRepository,
        JooqChatLinkRepository tgChatLinkRepository,
        JooqLinkRepository linkRepository
    ) {
        return new DefaultChatService(tgChatRepository, tgChatLinkRepository, linkRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "database.accessor", havingValue = "jooq")
    public LinkService jooqLinkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository tgChatLinkRepository,
        InformationProviders informationProviders
    ) {
        return new DefaultLinkService(linkRepository, tgChatLinkRepository, informationProviders);
    }

}
