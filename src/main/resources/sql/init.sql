CREATE
SEQUENCE gitproxy_sequence INCREMENT BY 30 MINVALUE = 1 MAXVALUE = 9223372036854775806;

create table user
(
    id            bigint      not null primary key,
    login         varchar(50) not null unique,
    request_count bigint      not null default 0
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE
OR
REPLACE FULLTEXT INDEX user_login_index
ON user (login);