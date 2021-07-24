create table file_meta (
   id       bigserial
       constraint file_meta_pkey
           primary key,
   path                 varchar(255),
   original_filename    varchar(255),
   content_type         varchar(255)
);
