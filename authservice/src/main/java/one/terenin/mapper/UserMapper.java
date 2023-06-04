package one.terenin.mapper;

import one.terenin.dto.UserForm;
import one.terenin.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserForm map(UserEntity entity);
    @InheritInverseConfiguration
    UserEntity map(UserForm response);
}
