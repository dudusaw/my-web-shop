create table product
(
    id          bigserial
        constraint product_pkey
            primary key,
    description varchar(1000),
    short_description varchar(255),
    price       double precision,
    rating      double precision,
    title       varchar(255),
    category_id bigint
        constraint fk__category_id
            references product_major_category
);

