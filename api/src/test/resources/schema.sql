create table audit
(
    audit_id     bigint auto_increment
        primary key,
    audit_object varchar(255) null,
    op_timestamp timestamp    null,
    operation    varchar(255) null,
    object_id    bigint       null
);

create table certificates
(
    certificate_id        bigint auto_increment
        primary key,
    created_date_time     timestamp      null,
    description           varchar(255)   null,
    duration              smallint       not null,
    is_archived           bit            not null,
    last_update_date_time timestamp      null,
    name                  varchar(45)    null,
    image_hash            varchar(45)    null,
    price                 decimal(10, 2) null
);

create table tags
(
    tag_id bigint auto_increment
        primary key,
    name   varchar(255) not null,
    constraint UK_t48xdq560gs3gap9g7jg36kgc
        unique (name)
);

create table certificate_tags
(
    certificate_id bigint not null,
    tag_id         bigint not null,
    constraint FK61tefnqygnlaie7u88d5ekps6
        foreign key (certificate_id) references certificates (certificate_id)
            on delete cascade,
    constraint FKp0o3qdgvfhh3vlj4wmk91kiqj
        foreign key (tag_id) references tags (tag_id)
            on delete cascade
);

CREATE TABLE users (
                       user_id bigint PRIMARY KEY AUTO_INCREMENT,
                       username varchar(45) NOT NULL,
                       password varchar(255) NOT NULL,
                       role varchar(255) NULL DEFAULT 'USER',
                       status varchar(255) NULL DEFAULT 'ACTIVE'
);


create table orders
(
    order_id     bigint auto_increment
        primary key,
    cost         decimal(10, 2) not null,
    puchase_date timestamp      not null,
    user_id      bigint         not null,
    constraint FK32ql8ubntj5uh44ph9659tiih
        foreign key (user_id) references users (user_id)
);

create table order_certificate
(
    amount         bigint         not null,
    price          decimal(10, 2) not null,
    certificate_id bigint         not null,
    order_id       bigint         not null,
    primary key (order_id, certificate_id),
    constraint FKop8aaofnl5q0a5r1t0p4t2c6v
        foreign key (certificate_id) references certificates (certificate_id),
    constraint FKrj5ul3jy6cil5g79jr6osfx5e
        foreign key (order_id) references orders (order_id)
);

