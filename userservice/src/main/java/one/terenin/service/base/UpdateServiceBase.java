package one.terenin.service.base;

import lombok.RequiredArgsConstructor;
import one.terenin.dto.UserForm;
import one.terenin.exception.children.APIException;
import one.terenin.exception.common.ErrorCode;
import one.terenin.mapper.UserMapper;
import one.terenin.repository.UserRepository;
import one.terenin.service.UpdateService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateServiceBase implements UpdateService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public Mono<UserForm> updatePhoto(String username, UUID photoId) {
        return repository.findByUsername(username)
                .flatMap(e -> {
                    if (photoId.equals(e.getPhotoId())){
                        return Mono.error(new APIException(ErrorCode.PICTURE_ALREADY_IN_USE));
                    }else {
                        e.setPhotoId(photoId);
                        return repository.save(e).map(mapper::map);
                    }
                })
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    public Mono<UserForm> updateFile(String username, UUID fileId) {
        return repository.findByUsername(username)
                .flatMap(e -> {
                    if (e.getFileId().contains(fileId)){
                        return Mono.error(new APIException(ErrorCode.FILE_ALREADY_ADDED));
                    }else {
                        e.getFileId().add(fileId);
                        return repository.save(e).map(mapper::map);
                    }
                })
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    public Mono<UserForm> updateUsername(String username, String newUsername) {
        return repository.findByUsername(username)
                .flatMap(e -> {
                    if (e.getUsername().equals(newUsername)){
                        return Mono.error(new APIException(ErrorCode.USERNAME_IN_USE));
                    }else {
                        e.setUsername(username);
                        return repository.save(e).map(mapper::map);
                    }
                })
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    public Mono<UserForm> updatePassword(String username, String newPassword) {
        return repository.findByUsername(username)
                .flatMap(e -> {
                    String encodedNewPassword = encoder.encode(newPassword);
                    if (e.getPassword().equals(encodedNewPassword)){
                        return Mono.error(new APIException(ErrorCode.PASSWORD_ALREADY_IN_USE));
                    }else {
                        e.setPassword(encodedNewPassword);
                        return repository.save(e).map(mapper::map);
                    }
                })
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    public Mono<UserForm> update(String username, UserForm form) {
        return repository.save(mapper.map(form)).map(mapper::map);
    }
}
