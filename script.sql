create table r_follow
(
    id          int auto_increment
        primary key,
    follower_id int               not null,
    followed_id int               not null,
    is_valid    tinyint default 1 not null,
    create_time datetime          not null
);

create table t_blog
(
    id           int auto_increment
        primary key,
    auth_id      int               not null,
    auth_account varchar(36)       null,
    title        varchar(128)      not null,
    description  varchar(256)      null,
    content      longtext          not null,
    permission   tinyint           not null,
    create_time  datetime          null,
    update_time  datetime          null,
    deleted      tinyint default 0 not null
);

create index INDEX_AUTH_ID
    on t_blog (auth_id);

create table t_user
(
    id           int auto_increment
        primary key,
    account      varchar(36)       null,
    password     varchar(36)       not null,
    name         varchar(18)       null,
    avatar       varchar(320)      null,
    phone        varchar(20)       null,
    email        varchar(36)       null,
    status       tinyint           not null,
    role         tinyint           not null,
    login_time   datetime          null,
    create_time  datetime          null,
    update_time  datetime          null,
    deleted      tinyint default 0 not null,
    blog_cnt     int     default 0 not null,
    followed_cnt int     default 0 not null,
    follower_cnt int     default 0 not null
);


