-- auto-generated definition
create table user
(
    uid      int          not null
        primary key,
    email    varchar(70)  not null,
    password varchar(200) not null,
    type     varchar(20)  not null,
    phone    varchar(45)  null,
    site     varchar(200) null,
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    del_date DATETIME NULL
);

