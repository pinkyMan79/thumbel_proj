package one.terenin.api;

import one.terenin.dto.UserForm;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/user/bind")
public interface UserBindAPI {

    @PatchMapping("/photo/{username}/{photoId}")
    Mono<UserForm> bindPhoto(@PathVariable("username") String username,
                             @PathVariable UUID photoId);
    @PatchMapping("/file/{username}/{fileId}")
    Mono<UserForm> bindFile(@PathVariable("username") String username,
                            @PathVariable UUID fileId);

}
