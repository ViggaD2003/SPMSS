CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'))
);

CREATE INDEX IF NOT EXISTS SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
ON SPRING_AI_CHAT_MEMORY(conversation_id, `timestamp`);

--CREATE TABLE IF NOT EXISTS ai_session (
--  `id` varchar(36) NOT NULL,
--  `created_time` datetime(6) NOT NULL,
--  `edited_time` datetime(6) NOT NULL,
--  `user_id` INT NOT NULL,
--  `name` varchar(255) NOT NULL COMMENT 'Session name/title',
--  PRIMARY KEY (`id`),
--  KEY `ai_session_user_idx` (`user_id`),
--  FOREIGN KEY (`user_id`) REFERENCES `accounts`(`id`) ON DELETE CASCADE
--);
--
--CREATE TABLE IF NOT EXISTS session_conversations (
--  `id` INT NOT NULL,
--  `session_id` varchar(36) NOT NULL,
--  `conversation_id` varchar(36) NOT NULL COMMENT 'Links to SPRING_AI_CHAT_MEMORY.conversation_id',
--  `created_time` datetime(6) NOT NULL,
--  PRIMARY KEY (`id`),
--  UNIQUE KEY `unique_conversation` (`conversation_id`),
--  KEY `session_conversations_session_idx` (`session_id`),
--  KEY `session_conversations_conversation_idx` (`conversation_id`),
--  FOREIGN KEY (`session_id`) REFERENCES `ai_session`(`id`) ON DELETE CASCADE
--);