package one.terenin.controller;

import lombok.RequiredArgsConstructor;
import one.terenin.api.UserBindAPI;
import one.terenin.dto.UserForm;
import one.terenin.service.BindService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserBindController implements UserBindAPI {

    private final BindService bindService;

    @Override
    public Mono<UserForm> bindPhoto(String username, UUID photoId) {
        return bindService.bindPhoto(username, photoId);
    }

    @Override
    public Mono<UserForm> bindFile(String username, UUID fileId) {
        return bindService.bindFile(username, fileId);
    }
}
