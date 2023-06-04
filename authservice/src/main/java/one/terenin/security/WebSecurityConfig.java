package one.terenin.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.terenin.security.converter.TokenServerAuthenticationConverter;
import one.terenin.security.manager.AuthenticationManager;
import one.terenin.security.propertysource.JWTPropertySource;
import one.terenin.security.verify.JwtVerificator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    private final JWTPropertySource jwtPropertySource;

    private final String[] publicUrlPatterns = new String[]{"/api/v1/auth/login", "/api/v1/auth/register"};

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http,
                                              AuthenticationManager manager) {
        return http.csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers(publicUrlPatterns)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((exchange, ex) -> {
                    log.info(ex.getMessage());
                    log.error(Arrays.toString(ex.getStackTrace()));
                    return Mono.fromRunnable(() -> exchange
                            .getResponse()
                            .setStatusCode(HttpStatus.UNAUTHORIZED));
                })
                .accessDeniedHandler((exchange, denied) -> {
                    log.info(denied.getMessage());
                    log.error(Arrays.toString(denied.getStackTrace()));
                    return Mono.fromRunnable(() -> exchange
                            .getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                })
                .and()
                .addFilterAt(authenticationWebFilter(manager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter authenticationWebFilter(AuthenticationManager manager) {
        AuthenticationWebFilter webFilter = new AuthenticationWebFilter(manager);
        webFilter.setServerAuthenticationConverter(
                new TokenServerAuthenticationConverter(new JwtVerificator(jwtPropertySource.getJwtSecret())));
        webFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        return webFilter;
    }

}
