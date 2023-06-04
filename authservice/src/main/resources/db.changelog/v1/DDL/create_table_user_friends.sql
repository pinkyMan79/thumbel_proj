CREATE TABLE t_user (
                            id UUID PRIMARY KEY,
                            file_id UUID[],
                            photo_id UUID,
                            username VARCHAR(255) NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            bio TEXT,
                            role VARCHAR(255) NOT NULL,
                            first_name VARCHAR(255),
                            last_name VARCHAR(255),
                            enabled BOOLEAN,
                            created_at TIMESTAMP,
                            updated_at TIMESTAMP
);