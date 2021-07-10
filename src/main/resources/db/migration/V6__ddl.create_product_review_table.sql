create table product_review
(
    id         bigserial
        constraint product_review_pkey
            primary key,
    rating     integer,
    review     varchar(255),
    timestamp  timestamp,
    product_id bigint
        constraint fk__product_id
            references product,
    user_id    bigint
        constraint fk__user_id
            references "user"
);

alter table product_review
    owner to postgres;

