create table user_role
(
    id   bigserial
        constraint user_role_pkey
            primary key,
    name varchar(255)
);