CREATE TABLE IF NOT EXISTS event (
                                     id SERIAL PRIMARY KEY,
                                     action VARCHAR NOT NULL ,
                                     object_id VARCHAR NOT NULL ,
                                     event_time TIMESTAMP NOT NULL
);
