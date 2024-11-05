ALTER TABLE events ADD CONSTRAINT unique_events_key UNIQUE (ID);
ALTER TABLE users ADD CONSTRAINT unique_users_key UNIQUE (ID);
CREATE TABLE event_attendees
(
    event_id INT REFERENCES events("id") ON DELETE CASCADE NOT NULL,
    user_id  INT REFERENCES users("id") ON DELETE CASCADE NOT NULL
);