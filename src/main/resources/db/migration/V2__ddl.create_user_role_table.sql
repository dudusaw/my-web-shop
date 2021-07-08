create table user_role
(
    id   bigint generated by default as identity
        constraint user_role_pkey
            primary key,
    name varchar(255)
);

alter table user_role
    owner to postgres;