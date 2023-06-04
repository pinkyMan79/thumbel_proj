package one.terenin.security.service;

import one.terenin.entity.UserEntity;
import one.terenin.entity.common.UserRole;
import one.terenin.repository.UserRepository;
import one.terenin.security.propertysource.JWTPropertySource;
import one.terenin.security.token.TokenDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JWTPropertySource jwtPropertySource;

    @InjectMocks
    private SecurityService securityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticate() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEnabled(true);
        when(userRepository.findByUsername(anyString())).thenReturn(Mono.just(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtPropertySource.getJwtExpiration()).thenReturn(3600);
        when(jwtPropertySource.getJwtIssuer()).thenReturn("testissuer");
        when(jwtPropertySource.getJwtSecret()).thenReturn("testsecret");
        Mono<TokenDetails> result = securityService.authenticate("testuser", "testpassword");
        TokenDetails tokenDetails = result.block();
        assertEquals(tokenDetails.getUserId(), user.getId());
    }

    @Test
    public void testGenerateToken() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole(UserRole.USER);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("username", user.getUsername());
        TokenDetails tokenDetails = securityService.generateToken(user);
        assertEquals(tokenDetails.getIssuedAt(), new Date(), String.valueOf(1000));
        assertEquals(tokenDetails.getExpAt(), new Date(tokenDetails.getIssuedAt().getTime() + 3600 * 1000), String.valueOf(1000));
        assertEquals(tokenDetails.getToken().length(), 172);
    }
}