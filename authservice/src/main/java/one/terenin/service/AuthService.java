package one.terenin.service;

import one.terenin.dto.AuthRequest;
import one.terenin.dto.AuthResponse;
import one.terenin.entity.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthService {

    Mono<UserEntity> doRegister(UserEntity userEntity);

    Mono<AuthResponse> doLogin(AuthRequest request);

    Mono<UserEntity> doFindUserById(UUID id);

    Mono<UserEntity> doFindUserByUsername(String username);
}
