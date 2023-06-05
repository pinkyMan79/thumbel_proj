package one.terenin.service.base;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.terenin.dto.AuthRequest;
import one.terenin.dto.AuthResponse;
import one.terenin.entity.UserEntity;
import one.terenin.entity.common.UserRole;
import one.terenin.mapper.TokenMapper;
import one.terenin.repository.UserRepository;
import one.terenin.security.propertysource.JWTPropertySource;
import one.terenin.security.service.SecurityService;
import one.terenin.security.token.TokenDetails;
import one.terenin.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceBase implements AuthService {

    private final SecurityService securityService;
    private final UserRepository repository;
    private final TokenMapper tokenMapper;
    private final PasswordEncoder encoder;
    private final JWTPropertySource propertySource;

    @Override
    public Mono<UserEntity> doRegister(UserEntity user) {
        return repository.save(user.toBuilder()
                        .id(UUID.randomUUID())
                        .password(encoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()).doOnSuccess(u -> log.info("{}{}", "successfully register user with: \n", u.toString()));
    }

    @Override
    public Mono<AuthResponse> doLogin(AuthRequest request) {
        Mono<TokenDetails> authenticate = securityService
                .authenticate(request.getUsername(), request.getPassword());
        return authenticate.map(tokenMapper::map);
    }

    @Override
    public Mono<UserEntity> doFindUserById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Mono<UserEntity> doFindUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Mono<String> doUpdateAndParseToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(Base64.getEncoder()
                        .encodeToString(propertySource
                                .getJwtSecret().getBytes()))
                .parseClaimsJws(token).getBody().getIssuer();
        return Mono.just(username);
    }
}
