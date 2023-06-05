package one.terenin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.terenin.api.AuthApi;
import one.terenin.dto.AuthRequest;
import one.terenin.dto.AuthResponse;
import one.terenin.dto.UserForm;
import one.terenin.entity.UserEntity;
import one.terenin.mapper.UserMapper;
import one.terenin.service.AuthService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService service;
    private final UserMapper mapper;

    @Override
    public Mono<UserForm> register(UserForm user) {
        UserEntity entity = mapper.map(user);
        return service.doRegister(entity).map(mapper::map).doOnSuccess(userForm -> {
            log.info("{}{}", "success added user with params: \n", userForm.toString());
        });
    }

    @Override
    public Mono<AuthResponse> login(AuthRequest request) {
        return service.doLogin(request).doOnSuccess(response -> {
            log.info("{}{}", "success added user with params: \n", response.toString());
        });
    }

    @Override
    public Mono<String> parseJwt(String token) {
        return service.doUpdateAndParseToken(token);
    }
}
