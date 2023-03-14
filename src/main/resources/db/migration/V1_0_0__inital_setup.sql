-- User --

CREATE TABLE IF NOT EXISTS user
(
    id        INT                 NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created   DATETIME            NOT NULL,
    username  VARCHAR(256) UNIQUE NOT NULL,
    password  VARCHAR(256),
    authtoken VARCHAR(256)
);

INSERT INTO user (created, username, password, authtoken)
VALUES (UTC_TIMESTAMP(), 'Mathias', '', 'test');

-- Calendar --

CREATE TABLE IF NOT EXISTS event_content
(
    id          INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created     DATETIME NOT NULL,
    title       VARCHAR(256),
    description VARCHAR(512),
    location    VARCHAR(256),
    link        VARCHAR(2048)
);

CREATE TABLE IF NOT EXISTS event_category
(
    id      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(256),
    color   VARCHAR(32),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS recurrence
(
    id       INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    dtype    VARCHAR(256),
    start    DATETIME,
    end      DATETIME,
    offset   INT,
    weekDays VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS event_deviation
(
    id            INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created       DATETIME NOT NULL,
    oldOccurrence DATETIME,
    newOccurrence DATETIME,
    duration      INT,
    cancelled     BIT,
    content_id    INT,
    FOREIGN KEY (content_id) REFERENCES event_content (id)
);

CREATE TABLE IF NOT EXISTS event
(
    id            INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created       DATETIME NOT NULL,
    duration      INT,
    content_id    INT,
    category_id   INT,
    recurrence_id INT,
    user_id       INT,
    FOREIGN KEY (content_id) REFERENCES event_content (id),
    FOREIGN KEY (category_id) REFERENCES event_category (id),
    FOREIGN KEY (recurrence_id) REFERENCES recurrence (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

-- Flashcards --

CREATE TABLE IF NOT EXISTS flashcard_deck
(
    id      INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created DATETIME NOT NULL,
    name    VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS flashcard
(
    id                      INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created                 DATETIME NOT NULL,
    question                VARCHAR(2048),
    solution                VARCHAR(2048),
    deck_id                 INT      NOT NULL,
    ease_factor             DECIMAL(9, 2),
    dayInterval             DECIMAL(9, 2),
    last_answered           DATETIME,
    next_planned_occurrence DATETIME,
    learning_step           VARCHAR(64),
    total_studied_seconds   INT,
    count_answered_correct  INT,
    count_answered_total    INT,
    FOREIGN KEY (deck_id) REFERENCES flashcard_deck (id)
);
