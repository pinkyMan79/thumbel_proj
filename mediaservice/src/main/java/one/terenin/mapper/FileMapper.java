package one.terenin.mapper;

import one.terenin.dto.FileRequest;
import one.terenin.dto.FileResponse;
import one.terenin.entity.FileEntity;
import one.terenin.repository.FileRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileEntity map(FileRequest request);
    FileResponse map(FileEntity entity);
}
