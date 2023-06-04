package one.terenin.security.converter;

import lombok.RequiredArgsConstructor;
import one.terenin.security.verify.JwtVerificator;
import one.terenin.security.verify.ResultToAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

// по большему счёту это что-то вроде местных фильтров, т.к. servlet api тут нет вместо них реализованы интерцепторы
@RequiredArgsConstructor
public class TokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtVerificator verificator;
    private static final String PREFIX = "Bearer ";
    private static final Function<String, Mono<String>> cutter = uncut ->
            Mono.justOrEmpty(uncut.substring(PREFIX.length()));

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractTokenFromHeader(exchange)
                .flatMap(cutter)
                .flatMap(verificator::verify)
                .flatMap(ResultToAuthentication::authenticationMono);
    }

    private Mono<String> extractTokenFromHeader(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
