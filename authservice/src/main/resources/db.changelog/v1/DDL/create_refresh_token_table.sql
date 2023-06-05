CREATE TABLE t_user (
                        id UUID PRIMARY KEY ,
                        user_id UUID NOT NULL ,
                        token varchar(1024) ,
                        expire_date timestamp not null
);