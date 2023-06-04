package one.terenin.api;

import one.terenin.dto.UserForm;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/user/update")
public interface UserUpdateAPI {

    @PatchMapping("/photo/{username}")
    Mono<UserForm> updatePhoto(@PathVariable("username") String username,
                               @RequestParam UUID photoId);
    @PatchMapping("/file/{username}")
    Mono<UserForm> updateFile(@PathVariable("username") String username,
                              @RequestParam UUID fileId);
    @PatchMapping("/username/{username}")
    Mono<UserForm> updateUsername(@PathVariable("username") String username,
                                  @RequestParam String newUsername);
    @PatchMapping("/password/{username}")
    Mono<UserForm> updatePassword(@PathVariable("username") String username,
                                  @RequestParam String newPassword);
    @PatchMapping("/password/{username}")
    Mono<UserForm> update(@PathVariable String username,
                          @RequestBody UserForm form);

}
