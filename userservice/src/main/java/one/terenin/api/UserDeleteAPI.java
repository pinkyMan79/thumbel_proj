package one.terenin.api;

import one.terenin.dto.UserForm;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/user/delete")
public interface UserDeleteAPI {

    @DeleteMapping ("/photo/{username}")
    Mono<UserForm> deletePhoto(@PathVariable("username") String username,
                               @RequestParam UUID photoId);
    @DeleteMapping("/file/{username}")
    Mono<UserForm> deleteFile(@PathVariable("username") String username,
                              @RequestParam UUID fileId);
    @DeleteMapping("/password/{username}")
    Mono<UserForm> delete(@PathVariable String username);

}
