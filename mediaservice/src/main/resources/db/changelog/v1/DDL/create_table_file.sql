CREATE TABLE t_file (
                         fileId UUID PRIMARY KEY,
                         fileName varchar(255) not null,
                         fileLocation varchar(511) not null,
                         maintainer uuid not null
);