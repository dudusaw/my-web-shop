insert into user_role (name)
values ('USER'),
       ('ADMIN');

insert into user_entity (email, username, password)
values ('sample-mail123@gmail.com', 'alex', '$2a$12$VYh.If5dXIyCsjpvJyDb7e3nZ24s.iyDC53mtG/uAb6G0uFuCMwdC'),
       ('sample-mail321@gmail.com', 'alice', '$2a$12$VYh.If5dXIyCsjpvJyDb7e3nZ24s.iyDC53mtG/uAb6G0uFuCMwdC');

insert into user_to_role (user_id, role_id)
values (1, 1);

insert into user_to_role (user_id, role_id)
values (2, 2);