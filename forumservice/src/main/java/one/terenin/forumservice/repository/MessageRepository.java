package one.terenin.forumservice.repository;

import one.terenin.forumservice.entity.MessageEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends R2dbcRepository<MessageEntity, UUID> {
}
