package one.terenin.security.manager;

import lombok.RequiredArgsConstructor;
import one.terenin.entity.UserEntity;
import one.terenin.exception.children.AuthenticationException;
import one.terenin.exception.common.ErrorCode;
import one.terenin.repository.UserRepository;
import one.terenin.security.principal.PrincipalBasedOnId;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        PrincipalBasedOnId basedOnId = (PrincipalBasedOnId) authentication.getPrincipal();
        return userRepository.findById(basedOnId.getId())
                .filter(UserEntity::isEnabled)
                .map(user -> authentication)
                .switchIfEmpty(Mono.error(new AuthenticationException(ErrorCode.DISABLE)));
    }

}
