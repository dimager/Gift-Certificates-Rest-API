CREATE SCHEMA IF NOT EXISTS GIFT_CERTIFICATE_DB;

create table GIFT_CERTIFICATE
(
    ID               BIGINT auto_increment,
    NAME             VARCHAR(45),
    DESCRIPTION      VARCHAR(255),
    PRICE            DECIMAL(10,2),
    DURATION         SMALLINT,
    CREATE_DATE      TIMESTAMP,
    LAST_UPDATE_DATE TIMESTAMP,
    constraint GIFT_CERTIFICATE_PK
        primary key (ID)
);

create table TAG
(
    ID   BIGINT auto_increment,
    NAME VARCHAR(45),
    constraint TAG_PK
        primary key (ID)
);

create unique index TAG_NAME_UINDEX
    on TAG (NAME);

create table GIFT_CERTIFICATE_HAS_TAG
(
    GIFT_CERTIFICATE_ID BIGINT not null,
    TAG_ID              BIGINT not null,
    constraint GIFT_CERTIFICATE_HAS_TAG_PK
        primary key (GIFT_CERTIFICATE_ID, TAG_ID),
    constraint GIFT_CERTIFICATE_HAS_TAG_GIFT_CERTIFICATE_ID_FK
        foreign key (GIFT_CERTIFICATE_ID) references GIFT_CERTIFICATE (ID) on DELETE CASCADE ,
    constraint GIFT_CERTIFICATE_HAS_TAG_TAG_ID_FK
        foreign key (TAG_ID) references TAG (ID) on DELETE CASCADE
);

