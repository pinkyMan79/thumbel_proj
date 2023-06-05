package one.terenin.repository;

import one.terenin.entity.RefreshToken;
import one.terenin.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends R2dbcRepository<RefreshToken, UUID> {
  Mono<RefreshToken> findByToken(String token);

  @Modifying
  Mono<Void> deleteByUser(UUID user);
}