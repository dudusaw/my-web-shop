create table product
(
    id          bigserial
        constraint product_pkey
            primary key,
    description varchar(10000),
    short_description varchar(255),
    price       decimal(12,2),
    rating      double precision,
    title       varchar(255),
    category_id bigint
        constraint fk__category_id
            references product_major_category
);

