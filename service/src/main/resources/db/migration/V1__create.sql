create sequence group_sequence start 1 increment 1;
create sequence role_sequence start 1 increment 1;
create sequence student_sequence start 1 increment 1;
create sequence user_sequence start 1 increment 1;
create table groups
(
    id   int8         not null,
    name varchar(255) not null,
    primary key (id)
);
create table role
(
    id        int8 not null,
    role_enum varchar(255),
    primary key (id)
);
create table student
(
    id       int8         not null,
    email    varchar(255) not null,
    name     varchar(255) not null,
    group_id int8,
    primary key (id)
);
create table user_roles
(
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);
create table users
(
    id       int8         not null,
    email    varchar(255) not null,
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);
alter table if exists student
    add constraint student_email_uk unique (email);
alter table if exists users
    add constraint user_email_uk unique (email);
alter table if exists users
    add constraint username_uk unique (username);
alter table if exists student
    add constraint student_group_fk foreign key (group_id) references groups;
alter table if exists user_roles
    add constraint role_user_fk foreign key (role_id) references role;
alter table if exists user_roles
    add constraint user_role_fk foreign key (user_id) references users;
alter table if exists groups
    add constraint group_name_uk unique (name);
