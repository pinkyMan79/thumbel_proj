package one.terenin.repository;

import one.terenin.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {

    Mono<UserEntity> findByUsername(String username);
/*
    <S extends UserEntity> Flux<S> findAll(Example<S> example);

    <S extends UserEntity> Mono<Long> count(Example<S> example);

    <S extends UserEntity> Mono<Boolean> exists(Example<S> example);*/

}
