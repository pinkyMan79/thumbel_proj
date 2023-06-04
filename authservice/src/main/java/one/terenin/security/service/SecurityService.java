package one.terenin.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import one.terenin.entity.UserEntity;
import one.terenin.exception.children.AuthenticationException;
import one.terenin.exception.common.ErrorCode;
import one.terenin.repository.UserRepository;
import one.terenin.security.propertysource.JWTPropertySource;
import one.terenin.security.token.TokenDetails;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JWTPropertySource jwtPropertySource;

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new AuthenticationException(ErrorCode.DISABLE));
                    }
                    if (!encoder.matches(user.getPassword(), password)) {
                        return Mono.error(new AuthenticationException(ErrorCode.UNMATCHED));
                    }
                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                }).switchIfEmpty(Mono.error(new AuthenticationException(ErrorCode.INVALID_USERNAME)));
    }

    public TokenDetails generateToken(UserEntity entity) {
        Map<String, Object> claims = new HashedMap<>();
        claims.put("role", entity.getRole());
        claims.put("username", entity.getUsername());
        return generateToken(claims, entity.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Date exp = new Date(new Date().getTime() + jwtPropertySource.getJwtExpiration() * 1000L);
        return generateToken(exp, claims, subject);
    }

    private TokenDetails generateToken(Date expiration, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(jwtPropertySource.getJwtIssuer())
                .setIssuedAt(createdDate)
                .setExpiration(expiration)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(jwtPropertySource.getJwtSecret().getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expAt(expiration)
                .build();
    }

}
