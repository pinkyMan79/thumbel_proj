package one.terenin.service.base;

import lombok.RequiredArgsConstructor;
import one.terenin.entity.RefreshToken;
import one.terenin.repository.RefreshTokenRepository;
import one.terenin.repository.UserRepository;
import one.terenin.security.propertysource.JWTPropertySource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final JWTPropertySource propertySource;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  public Mono<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public Mono<RefreshToken> createRefreshToken(UUID userId) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(UUID.randomUUID().toString());
    return refreshTokenRepository.save(refreshToken.toBuilder()
                    .user(userId)
                    .id(UUID.randomUUID())
                    .expiryDate(Instant.now().plusMillis(propertySource.getJwtExpiration().longValue()))
            .build());
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token).subscribe();
      throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  public Mono<Void> deleteByUserId(UUID userId) {
    return refreshTokenRepository.deleteByUser(userId);
  }
}