package one.terenin.api;

import one.terenin.dto.AuthRequest;
import one.terenin.dto.AuthResponse;
import one.terenin.dto.UserForm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @PostMapping("/register")
    Mono<UserForm> register(@RequestBody UserForm user);

    @GetMapping("/login")
    Mono<AuthResponse> login(@RequestBody AuthRequest request);

    @GetMapping("/parse-and-update/{token}")
    Mono<String> parseJwt(@PathVariable("token") String token);

}
