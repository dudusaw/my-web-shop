create table product
(
    id          bigint generated by default as identity
        constraint product_pkey
            primary key,
    description varchar(255),
    price       double precision,
    rating      double precision,
    title       varchar(255),
    category_id bigint
        constraint fk__category_id
            references product_major_category
);

alter table product
    owner to postgres;

