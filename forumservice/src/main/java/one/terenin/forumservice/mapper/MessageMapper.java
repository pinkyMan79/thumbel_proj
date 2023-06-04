package one.terenin.forumservice.mapper;

import one.terenin.forumservice.entity.MessageEntity;
import one.terenin.forumservice.grpc.MessageRequest;
import one.terenin.forumservice.grpc.MessageResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageEntity map(MessageRequest request);
    @InheritInverseConfiguration
    MessageResponse map(MessageEntity entity);
}
