create table product_to_file
(
    id           bigserial not null
        constraint product_to_file_pk
            primary key,
    product_id   bigint
        constraint fk__product_id
            references product,
    file_meta_id bigint
        constraint fk__file_meta_id
            references file_meta
);