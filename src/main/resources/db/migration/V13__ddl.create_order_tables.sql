create table order_entity
(
    id bigserial
        constraint order_pk
            primary key,
    user_id bigint
        constraint fk__user_id
            references user_entity,
    timestamp timestamp,
    total_price decimal(12, 2)
);

create table order_product
(
    id bigserial
        constraint order_to_products_pk
            primary key,
    order_id bigint
        constraint order_to_products_order_entity_id_fk
            references order_entity,
    product_id bigint
        constraint order_to_products_product_id_fk
            references product,
    count   int
);
