package one.terenin.forumservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Table(name = "t_forum")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ForumEntity{

    private UUID forumId;

    private String title;

    private String description;

    private Set<UUID> messages;

}
