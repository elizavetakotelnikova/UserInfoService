CREATE TYPE color AS enum (
       'orange',
       'black',
       'white',
       'grey',
       'colourful'
);

CREATE TABLE IF NOT EXISTS cats (
                       id BIGINT PRIMARY KEY,
                       name VARCHAR(255),
                       breed VARCHAR(255),
                       color color,
                       owner_id BIGINT NOT NULL,
                       birthday DATE);
CREATE TABLE IF NOT EXISTS friends (
                      first_cat_id BIGINT NOT NULL,
                      second_cat_id BIGINT NOT NULL,
                      primary key(first_cat_id, second_cat_id)
);

CREATE TABLE IF NOT EXISTS owners (
                    id BIGINT NOT NULL,
                    birthday DATE
                    );