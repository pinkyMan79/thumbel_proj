CREATE TABLE t_photo (
                         photoId UUID PRIMARY KEY,
                         checksum int8 not null default 0,
                         name varchar(255) not null
);