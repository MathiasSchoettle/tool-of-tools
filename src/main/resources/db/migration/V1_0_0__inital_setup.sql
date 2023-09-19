-- User --

CREATE TABLE IF NOT EXISTS user
(
    id         INT                 NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created    DATETIME            NOT NULL,
    username   VARCHAR(256) UNIQUE NOT NULL,
    password   VARCHAR(256),
    auth_token VARCHAR(256)
);

INSERT INTO user (created, username, password, auth_token)
VALUES (UTC_TIMESTAMP(), 'Test User', '', 'test');

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
    id        INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    dtype     VARCHAR(256),
    start     DATETIME,
    end       DATETIME,
    offset    INT,
    week_days VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS event
(
    id            INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created       DATETIME NOT NULL,
    duration      INT,
    full_day      BIT,
    content_id    INT,
    category_id   INT,
    recurrence_id INT,
    user_id       INT,
    FOREIGN KEY (content_id) REFERENCES event_content (id),
    FOREIGN KEY (category_id) REFERENCES event_category (id),
    FOREIGN KEY (recurrence_id) REFERENCES recurrence (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS event_deviation
(
    id             INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created        DATETIME NOT NULL,
    event_id       INT,
    old_occurrence DATETIME,
    new_occurrence DATETIME,
    duration       INT,
    cancelled      BIT,
    content_id     INT,
    FOREIGN KEY (event_id) REFERENCES event (id),
    FOREIGN KEY (content_id) REFERENCES event_content (id)
);

-- Flashcards --

CREATE TABLE IF NOT EXISTS flashcard_deck
(
    id      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created DATETIME,
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
    day_interval            DECIMAL(9, 2),
    last_answered           DATETIME,
    next_planned_occurrence DATETIME,
    learning_step           VARCHAR(64),
    total_studied_seconds   INT,
    count_answered_correct  INT,
    count_answered_total    INT,
    FOREIGN KEY (deck_id) REFERENCES flashcard_deck (id)
);
