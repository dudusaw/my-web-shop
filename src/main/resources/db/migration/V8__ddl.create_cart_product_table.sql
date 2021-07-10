create table cart_product
(
    id bigserial
        constraint cart_product_pkey
            primary key,
    count             integer,
    product_id        bigint
        constraint fk__product_id
            references product,
    user_id           bigint
        constraint fk__user_id
            references "user",
    cart_products_key bigint
);

alter table cart_product
    owner to postgres;

