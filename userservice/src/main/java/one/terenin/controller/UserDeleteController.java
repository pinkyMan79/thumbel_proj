package one.terenin.controller;

import lombok.RequiredArgsConstructor;
import one.terenin.api.UserDeleteAPI;
import one.terenin.dto.UserForm;
import one.terenin.service.DeleteService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserDeleteController implements UserDeleteAPI {

    private final DeleteService deleteService;

    @Override
    public Mono<UserForm> deletePhoto(String username, UUID photoId) {
        return deleteService.deletePhoto(username, photoId);
    }

    @Override
    public Mono<UserForm> deleteFile(String username, UUID fileId) {
        return deleteService.deleteFile(username, fileId);
    }

    @Override
    public Mono<UserForm> delete(String username) {
        return deleteService.delete(username);
    }
}
