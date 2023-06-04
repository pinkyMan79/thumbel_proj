package one.terenin.service.base;

import lombok.RequiredArgsConstructor;
import one.terenin.dto.UserForm;
import one.terenin.entity.UserEntity;
import one.terenin.exception.children.APIException;
import one.terenin.exception.common.ErrorCode;
import one.terenin.mapper.UserMapper;
import one.terenin.repository.UserRepository;
import one.terenin.service.DeleteService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteServiceBase implements DeleteService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<UserForm> deletePhoto(String username, UUID photoId) {
        return repository.findByUsername(username)
                .flatMap(e -> {
                    if (e.getPhotoId().equals(photoId)){
                        e.setPhotoId(null);
                        return repository.save(e)
                                .map(mapper::map);
                    }else {
                        return Mono.error(new APIException(ErrorCode.NOT_A_USER_PICTURE));
                    }
                })
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    public Mono<UserForm> deleteFile(String username, UUID fileId) {
        return repository.findByUsername(username)
                .flatMap(e -> {
                    if (e.getFileId().contains(fileId)) {
                        e.getFileId().remove(fileId);
                        return repository.save(e)
                                .map(mapper::map);
                    } else {
                        return Mono.error(new APIException(ErrorCode.NO_SUCH_FILE));
                    }
                })
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    public Mono<UserForm> delete(String username) {
        return null;
    }
}
