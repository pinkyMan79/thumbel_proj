package one.terenin.security.verify;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import one.terenin.exception.children.AuthenticationException;
import one.terenin.exception.children.JwtException;
import one.terenin.exception.common.ErrorCode;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

// not a component
public class JwtVerificator {

    private final String secret;

    public JwtVerificator(String secret) {
        this.secret = secret;
    }

    public record VerificationResult(Claims claims, String token) { }

    public Mono<VerificationResult> verify(String accessToken) {
        return Mono.just(checkToken(accessToken))
                .onErrorResume(e -> Mono
                        .error(new JwtException(ErrorCode.UNAUTHORIZED)));
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    private VerificationResult checkToken(String accessToken) {
        Claims claims = extractClaimsFromToken(accessToken);
        Date exp = claims.getExpiration();

        if (exp.before(new Date())) {
            throw new AuthenticationException(ErrorCode.JWT_EXPIRED);
        }
        return new VerificationResult(claims, accessToken);
    }

}
