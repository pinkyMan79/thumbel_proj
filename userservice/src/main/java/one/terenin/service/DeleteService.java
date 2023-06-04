package one.terenin.service;

import one.terenin.dto.UserForm;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeleteService {

    Mono<UserForm> deletePhoto(String username, UUID photoId);
    Mono<UserForm> deleteFile(String username, UUID fileId);
    Mono<UserForm> delete(String username);
}
