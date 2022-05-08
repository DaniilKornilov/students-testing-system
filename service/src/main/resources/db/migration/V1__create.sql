create sequence answer_sequence start 1 increment 1;
create sequence course_sequence start 1 increment 1;
create sequence group_sequence start 1 increment 1;
create sequence image_sequence start 1 increment 1;
create sequence role_sequence start 1 increment 1;
create sequence student_answer_sequence start 1 increment 1;
create sequence student_sequence start 1 increment 1;
create sequence student_task_sequence start 1 increment 1;
create sequence student_test_sequence start 1 increment 1;
create sequence task_sequence start 1 increment 1;
create sequence teacher_sequence start 1 increment 1;
create sequence test_sequence start 1 increment 1;
create sequence user_sequence start 1 increment 1;
create table answer
(
    id      int8         not null,
    name    varchar(255) not null,
    value   float8       not null,
    task_id int8         not null,
    primary key (id)
);
create table course
(
    id         int8         not null,
    name       varchar(255) not null,
    teacher_id int8         not null,
    primary key (id)
);
create table course_group
(
    course_id int8 not null,
    group_id  int8 not null,
    primary key (course_id, group_id)
);
create table groups
(
    id   int8         not null,
    name varchar(255) not null,
    primary key (id)
);
create table image
(
    id      int8         not null,
    path    varchar(255) not null,
    task_id int8         not null,
    primary key (id)
);
create table role
(
    id             int8 not null,
    role_enum_name varchar(255),
    primary key (id)
);
create table student
(
    id         int8         not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    group_id   int8         not null,
    user_id    int8         not null,
    primary key (id)
);
create table student_answer
(
    id              int8         not null,
    name            varchar(255) not null,
    value           float8       not null,
    student_task_id int8         not null,
    primary key (id)
);
create table student_task
(
    id                 int8      not null,
    finished_timestamp timestamp not null,
    is_correct         boolean   not null,
    started_timestamp  timestamp not null,
    student_test_id    int8      not null,
    task_id            int8      not null,
    primary key (id)
);
create table student_test
(
    id                 int8      not null,
    finished_timestamp timestamp not null,
    started_timestamp  timestamp not null,
    student_id         int8      not null,
    test_id            int8      not null,
    primary key (id)
);
create table task
(
    id          int8         not null,
    description TEXT         not null,
    name        varchar(255) not null,
    test_id     int8         not null,
    primary key (id)
);
create table teacher
(
    id         int8         not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    user_id    int8         not null,
    primary key (id)
);
create table test
(
    id                int8         not null,
    created_timestamp timestamp    not null,
    description       TEXT         not null,
    name              varchar(255) not null,
    time_limit        time,
    updated_timestamp timestamp    not null,
    course_id         int8         not null,
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
alter table if exists course
    add constraint course_name_uk unique (name);
alter table if exists groups
    add constraint group_name_uk unique (name);
alter table if exists users
    add constraint user_email_uk unique (email);
alter table if exists users
    add constraint username_uk unique (username);
alter table if exists answer
    add constraint answer_task_fk foreign key (task_id) references task;
alter table if exists course
    add constraint course_teacher_fk foreign key (teacher_id) references teacher;
alter table if exists course_group
    add constraint group_course_fk foreign key (group_id) references groups;
alter table if exists course_group
    add constraint course_group_fk foreign key (course_id) references course;
alter table if exists image
    add constraint image_task_fk foreign key (task_id) references task;
alter table if exists student
    add constraint student_group_fk foreign key (group_id) references groups;
alter table if exists student
    add constraint student_user_fk foreign key (user_id) references users;
alter table if exists student_answer
    add constraint student_answer_student_task_fk foreign key (student_task_id) references student_task;
alter table if exists student_task
    add constraint student_task_student_test_fk foreign key (student_test_id) references student_test;
alter table if exists student_task
    add constraint student_task_task_fk foreign key (task_id) references task;
alter table if exists student_test
    add constraint student_test_student_fk foreign key (student_id) references student;
alter table if exists student_test
    add constraint student_test_test_fk foreign key (test_id) references test;
alter table if exists task
    add constraint task_test_fk foreign key (test_id) references test;
alter table if exists teacher
    add constraint teacher_user_fk foreign key (user_id) references users;
alter table if exists test
    add constraint test_course_fk foreign key (course_id) references course;
alter table if exists user_roles
    add constraint role_user_fk foreign key (role_id) references role;
alter table if exists user_roles
    add constraint user_role_fk foreign key (user_id) references users;
