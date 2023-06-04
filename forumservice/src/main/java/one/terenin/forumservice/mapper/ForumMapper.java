package one.terenin.forumservice.mapper;

import one.terenin.forumservice.entity.ForumEntity;
import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ForumMapper {
    ForumEntity map(ForumCreationRequest request);
    ForumResponse map(ForumGetRequest request);
    @InheritInverseConfiguration
    ForumResponse map(ForumEntity entity);
}
