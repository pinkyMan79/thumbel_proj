package one.terenin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table(name = "t_file")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    private UUID fileId;
    private String fileName;
    private String fileLocation;
    private UUID maintainer;

}
