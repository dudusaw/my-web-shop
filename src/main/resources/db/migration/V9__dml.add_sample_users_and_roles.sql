insert into user_role (name)
values ('USER');

insert into user_role (name)
values ('ADMIN');

insert into user_entity (email, username, password)
values ('sample-mail123@gmail.com', 'alex', '$2y$12$wxtP1xbDfCRZ96OTFp6wfONsB.IKOEa448r3CxeW9y6lbVtf39q4.');

insert into user_entity (email, username, password)
values ('sample-mail321@gmail.com', 'alice', '$2y$12$wxtP1xbDfCRZ96OTFp6wfONsB.IKOEa448r3CxeW9y6lbVtf39q4.');

insert into user_to_role (user_id, role_id)
values (1, 1);

insert into user_to_role (user_id, role_id)
values (2, 2);