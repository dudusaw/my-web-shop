create table user_entity
(
    id       bigserial
        constraint user_pkey
            primary key,
    email    varchar(255),
    password varchar(255),
    username varchar(255)
        constraint uk_username
            unique
);

