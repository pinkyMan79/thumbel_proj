package one.terenin.mapper;

import one.terenin.dto.AuthResponse;
import one.terenin.security.token.TokenDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenDetails map(AuthResponse response);
    @InheritInverseConfiguration
    AuthResponse map(TokenDetails details);
}
