create table file_meta (
   id       bigserial
       constraint file_meta_pkey
           primary key,
   path                 varchar(255),
   original_filename    varchar(255)
);

alter table product
    add column image_file_id bigint
        constraint fk__image_file_id
            references file_meta;