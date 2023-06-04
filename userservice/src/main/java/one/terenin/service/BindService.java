package one.terenin.service;

import one.terenin.dto.UserForm;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BindService {

    Mono<UserForm> bindPhoto(String username, UUID photoId);
    Mono<UserForm> bindFile(String username, UUID fileId);

}
