-- we don't know how to generate root <with-no-name> (class Root) :(
grant select, system_user on *.* to 'mysql.infoschema'@localhost;

grant backup_admin, clone_admin, connection_admin, persist_ro_variables_admin, session_variables_admin, shutdown, super, system_user, system_variables_admin on *.* to 'mysql.session'@localhost;

grant system_user on *.* to 'mysql.sys'@localhost;

grant alter, alter routine, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, event, execute, file, index, insert, lock tables, process, references, reload, replication client, replication slave, select, set_user_id, show databases, show view, shutdown, super, system_user, trigger, update, grant option on *.* to rdsadmin@localhost;

grant alter, alter routine, create, create routine, create temporary tables, create user, create view, delete, drop, event, execute, index, insert, lock tables, process, references, reload, replication client, replication slave, select, show databases, show view, trigger, update, grant option on *.* to waspy;

create table authority
(
    authority_name varchar(50) not null,
    idx int auto_increment
        primary key
);

create table demand_position
(
    idx int auto_increment
        primary key,
    position_idx int not null,
    study_announcement_idx int not null,
    demand int not null,
    applied int default 0 null
);

create table education
(
    idx int auto_increment
        primary key,
    name varchar(45) null
);

create table portfolio
(
    idx int auto_increment
        primary key,
    title varchar(100) not null,
    content text null,
    user_uid varchar(45) null,
    site varchar(200) null,
    reg_date datetime default CURRENT_TIMESTAMP null,
    education_idx int null
);

create table portfolio_position
(
    portfolio_idx int not null,
    position_idx int not null,
    primary key (portfolio_idx, position_idx)
);

create table position
(
    idx int auto_increment
        primary key,
    name varchar(100) not null,
    constraint name_UNIQUE
        unique (name)
);

create table project
(
    idx int auto_increment
        primary key,
    portfolio_idx int null,
    title varchar(200) not null,
    content text not null,
    site varchar(255) null
);

create table project_stack
(
    project_idx int not null,
    stack_idx int not null
);

create table stack
(
    name varchar(100) not null,
    idx int auto_increment
        primary key,
    content text null,
    constraint name_UNIQUE
        unique (name)
);

create table study
(
    idx int auto_increment
        primary key,
    user_uid int null,
    title varchar(200) not null,
    content text null,
    reg_date datetime default CURRENT_TIMESTAMP not null,
    study_category_idx int null
);

create table study_announcement
(
    idx int auto_increment
        primary key,
    study_idx int null,
    title varchar(200) null,
    content text null,
    reg_date datetime default CURRENT_TIMESTAMP null,
    activated tinyint default 0 null,
    end_date datetime null
);

create table study_application
(
    idx int auto_increment
        primary key,
    study_announcement_idx int not null,
    portfolio_idx int not null,
    reg_date datetime default CURRENT_TIMESTAMP not null,
    position_idx int not null,
    declined int default 0 not null
);

create table study_category
(
    idx int auto_increment
        primary key,
    title varchar(100) not null,
    content text null,
    constraint title_UNIQUE
        unique (title)
);

create table study_user
(
    idx int auto_increment
        primary key,
    user_uid int null,
    study_idx int null,
    reg_date datetime default CURRENT_TIMESTAMP null,
    position_idx int null
);

create table tech
(
    idx int auto_increment
        primary key,
    portfolio_idx int null,
    content text null,
    ability int null,
    stack_idx int not null
);

create table tech_stack
(
    tech_idx int not null,
    stack_idx int not null,
    primary key (tech_idx, stack_idx)
);

create table ui_image
(
    idx int auto_increment
        primary key,
    name varchar(255) not null,
    original_name varchar(255) not null,
    save_name varchar(255) not null,
    size int null,
    upload_path varchar(255) not null,
    constraint name_UNIQUE
        unique (name)
);

create table user
(
    uid int auto_increment
        primary key,
    username varchar(50) not null,
    password varchar(200) not null,
    phone varchar(45) null,
    site varchar(255) null,
    reg_date datetime default CURRENT_TIMESTAMP not null,
    del_date datetime null,
    activated tinyint null,
    name varchar(45) null,
    constraint username_UNIQUE
        unique (username)
);

create table user_authority
(
    user_uid int not null,
    authority_idx int not null,
    primary key (user_uid, authority_idx)
);

