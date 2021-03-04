create table document (
    id int4 not null,
    creation_date timestamp,
    file_name varchar(255),
    status varchar(255),
    name varchar(255),
    primary key (id)
);

create table document_users (
    document_id int4 not null,
    users_id int4 not null,
    primary key (document_id, users_id)
);

create table user_role (
    user_id int4 not null,
    roles varchar(255)
);

create table usr (
    id int4 not null,
    login varchar(255),
    password varchar(255),
    primary key (id)
);