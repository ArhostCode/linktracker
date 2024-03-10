--liquibase formatted sql

--changeset arhostcode:1
CREATE TABLE IF NOT EXISTS tg_chat
(
    id BIGINT NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS link
(
    id              BIGINT PRIMARY KEY,
    url             TEXT UNIQUE,
    description     TEXT NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    last_checked_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id BIGINT REFERENCES tg_chat (id),
    link_id BIGINT REFERENCES link (id),
    PRIMARY KEY (chat_id, link_id)
);

--rollback drop table tg_chat,link,chat_link;
