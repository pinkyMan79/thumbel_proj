package one.terenin.security.principal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalBasedOnId implements Principal {
    private UUID id;
    private String name;
}
