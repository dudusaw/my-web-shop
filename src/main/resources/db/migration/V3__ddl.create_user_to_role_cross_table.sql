create table user_to_role
(
    user_id bigint not null
        constraint fk__user_id
            references user_entity,
    role_id bigint not null
        constraint fk__role_id
            references user_role
);