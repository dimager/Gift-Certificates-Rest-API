CREATE SCHEMA IF NOT EXISTS PUBLIC;
CREATE TABLE IF NOT EXISTS audit (
                                     audit_id bigint PRIMARY KEY AUTO_INCREMENT,
                                     audit_object varchar(255) NULL,
                                     op_timestamp timestamp NULL,
                                     operation varchar(255) NULL,
                                     object_id bigint NULL
);
CREATE TABLE IF NOT EXISTS certificates (
                                            certificate_id bigint PRIMARY KEY AUTO_INCREMENT,
                                            created_date_time timestamp NULL,
                                            description varchar(255) NULL,
                                            duration smallint NOT NULL,
                                            is_archived bit NOT NULL,
                                            last_update_date_time timestamp NULL,
                                            name varchar(45) NULL,
                                            price decimal(10, 2) NULL
);
CREATE TABLE IF NOT EXISTS tags (
                                    tag_id bigint PRIMARY KEY AUTO_INCREMENT,
                                    name varchar(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS certificate_tags (
                                                certificate_id bigint NOT NULL,
                                                tag_id bigint NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
                                     user_id bigint PRIMARY KEY AUTO_INCREMENT,
                                     username varchar(45) NOT NULL
);
CREATE TABLE IF NOT EXISTS orders (
                                      order_id bigint PRIMARY KEY AUTO_INCREMENT,
                                      cost decimal(10, 2) NOT NULL,
                                      puchase_date timestamp NOT NULL,
                                      user_id bigint NOT NULL
);
CREATE TABLE IF NOT EXISTS order_certificate (
                                                 amount bigint NOT NULL,
                                                 price decimal(10, 2) NOT NULL,
                                                 certificate_id bigint NOT NULL,
                                                 order_id bigint NOT NULL
);
