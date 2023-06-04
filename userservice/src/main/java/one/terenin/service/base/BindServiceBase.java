package one.terenin.service.base;

import lombok.RequiredArgsConstructor;
import one.terenin.dto.UserForm;
import one.terenin.entity.UserEntity;
import one.terenin.mapper.UserMapper;
import one.terenin.repository.UserRepository;
import one.terenin.service.BindService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BindServiceBase implements BindService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public Mono<UserForm> bindPhoto(String username, UUID photoId) {
        Mono<UserEntity> entity = repository.findByUsername(username);
        entity.doOnSuccess(e -> {
            e.setPhotoId(photoId);
            repository.save(e);
        });
        return entity.map(mapper::map);
    }

    @Override
    public Mono<UserForm> bindFile(String username, UUID fileId) {
        Mono<UserEntity> entity = repository.findByUsername(username);
        entity.doOnSuccess(e -> {
            List<UUID> fileId1 = e.getFileId();
            fileId1.add(fileId);
            e.setFileId(fileId1);
            repository.save(e);
        });
        return entity.map(mapper::map);
    }
}
