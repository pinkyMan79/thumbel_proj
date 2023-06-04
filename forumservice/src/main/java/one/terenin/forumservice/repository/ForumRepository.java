package one.terenin.forumservice.repository;

import one.terenin.forumservice.entity.ForumEntity;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Repository
public interface ForumRepository extends R2dbcRepository<ForumEntity, UUID> {

    Mono<ForumEntity> findByTitle(String title);
    Mono<Void> deleteForumEntityByTitle(String title);
}
