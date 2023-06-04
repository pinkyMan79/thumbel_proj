package one.terenin.service;

import one.terenin.dto.UserForm;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UpdateService {

    Mono<UserForm> updatePhoto(String username, UUID photoId);
    Mono<UserForm> updateFile(String username, UUID fileId);
    Mono<UserForm> updateUsername(String username, String newUsername);
    Mono<UserForm> updatePassword(String username, String newPassword);
    Mono<UserForm> update(String username, UserForm form);

}
