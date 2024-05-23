ALTER TABLE users
DROP column owner_id;

ALTER TABLE owners
ADD column user_id BIGINT not null default 1 REFERENCES users(id);