package one.terenin.forumservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table(name = "t_msg")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    private UUID messageId;

    private String content;

    private UUID forum;

    private String forumTitle;

    private String senderLogin;

}
