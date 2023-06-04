package one.terenin.controller;

import lombok.RequiredArgsConstructor;
import one.terenin.api.UserUpdateAPI;
import one.terenin.dto.UserForm;
import one.terenin.service.UpdateService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserUpdateController implements UserUpdateAPI {

    private final UpdateService updateService;

    @Override
    public Mono<UserForm> updatePhoto(String username, UUID photoId) {
        return updateService.updatePhoto(username, photoId);
    }

    @Override
    public Mono<UserForm> updateFile(String username, UUID fileId) {
        return updateService.updateFile(username, fileId);
    }

    @Override
    public Mono<UserForm> updateUsername(String username, String newUsername) {
        return updateService.updateUsername(username, newUsername);
    }

    @Override
    public Mono<UserForm> updatePassword(String username, String newPassword) {
        return updateService.updatePassword(username, newPassword);
    }

    @Override
    public Mono<UserForm> update(String username, UserForm form) {
        return updateService.update(username, form);
    }
}
