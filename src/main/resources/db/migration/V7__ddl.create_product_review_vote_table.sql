create table product_review_vote
(
    id        bigserial
        constraint product_review_vote_pkey
            primary key,
    positive  boolean not null,
    review_id bigint
        constraint fk__review_id
            references product_review,
    user_id   bigint
        constraint fk__user_id
            references user_entity
);

