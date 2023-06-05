package one.terenin.repository;

import one.terenin.entity.FileEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends R2dbcRepository<FileEntity, UUID> {

}
