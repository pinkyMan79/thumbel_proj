package one.terenin.security.verify;

import io.jsonwebtoken.Claims;
import one.terenin.security.principal.PrincipalBasedOnId;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public class ResultToAuthentication {

    public static Mono<Authentication> authenticationMono(
            JwtVerificator.VerificationResult verificationResult) {
        Claims claims = verificationResult.claims();
        String subject = claims.getSubject();
        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UUID principalId = UUID.fromString(subject);
        PrincipalBasedOnId onId = new PrincipalBasedOnId(principalId, username);
        return Mono.just(new UsernamePasswordAuthenticationToken(onId, null, authorities));
    }

}
