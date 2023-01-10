DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    username        varchar(500)   NOT NULL DEFAULT '' COMMENT 'username',
    password   varchar(200)   NOT NULL DEFAULT '' COMMENT 'password',
    age     int  NOT NULL DEFAULT '0' COMMENT 'age',
    PRIMARY KEY (`username`)
)