package one.terenin.service;

import one.terenin.dto.UserForm;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BindService {

    Mono<UserForm> bindPhoto(String username, UUID photoId);
    Mono<UserForm> bindFile(String username, UUID fileId);

}
